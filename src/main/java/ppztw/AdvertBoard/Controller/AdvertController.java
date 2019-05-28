package ppztw.AdvertBoard.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ppztw.AdvertBoard.Advert.AdvertService;
import ppztw.AdvertBoard.Advert.AdvertUserService;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Payload.Advert.CreateAdvertRequest;
import ppztw.AdvertBoard.Payload.Advert.EditAdvertRequest;
import ppztw.AdvertBoard.Payload.Advert.ReportAdvertRequest;
import ppztw.AdvertBoard.Payload.ApiResponse;
import ppztw.AdvertBoard.Report.ReportService;
import ppztw.AdvertBoard.Repository.Advert.AdvertRepository;
import ppztw.AdvertBoard.Repository.UserRepository;
import ppztw.AdvertBoard.Security.CurrentUser;
import ppztw.AdvertBoard.Security.UserPrincipal;
import ppztw.AdvertBoard.Stats.StatsService;
import ppztw.AdvertBoard.User.UserService;
import ppztw.AdvertBoard.View.Advert.AdvertDetailsView;
import ppztw.AdvertBoard.View.Advert.AdvertSummaryView;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/advert")
public class AdvertController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvertUserService advertUserService;

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private AdvertService advertService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private StatsService statsService;

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public ResponseEntity<?> addAdvert2(@CurrentUser UserPrincipal userPrincipal,
                                       @Valid @RequestParam("title") @NotBlank String title,
                                       @Valid @RequestParam("tags") @Nullable List<String> tags,
                                       @Valid @RequestParam("description") @NotBlank String description,
                                       @Valid @RequestParam("category") @NotNull Long category,
                                       @Valid @RequestParam("additionalInfo") @Nullable Map<String, String> additionalInfo,
                                       @Valid @RequestParam("imageFile") @Nullable MultipartFile imageFile) {

        CreateAdvertRequest createAdvertRequest = new CreateAdvertRequest();
        createAdvertRequest.setTitle(title);
        createAdvertRequest.setTags(tags);
        createAdvertRequest.setDescription(description);
        createAdvertRequest.setCategory(category);
        createAdvertRequest.setAdditionalInfo(additionalInfo);
        createAdvertRequest.setImageFile(imageFile);

        advertUserService.addAdvert(userPrincipal.getId(), createAdvertRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Added new advert"));
    }

    @PostMapping(value = "/edit")
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public ResponseEntity<?> editAdvert(@CurrentUser UserPrincipal userPrincipal,
                                        @Valid @RequestBody EditAdvertRequest editAdvertRequest) {

        advertUserService.editAdvert(userPrincipal.getId(), editAdvertRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Advert edited successfully"));
    }

    @PostMapping("remove")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> removeAdvert(@CurrentUser UserPrincipal userPrincipal,
                                          @RequestParam Long id) {
        Advert advert = advertUserService.findAdvert(userPrincipal.getId(), id).orElseThrow(() ->
                new ResourceNotFoundException("Advert", "id", id));

        advert.setStatus(Advert.Status.ARCHIVED);
        advertRepository.save(advert);
        return ResponseEntity.ok(new ApiResponse(true, "Advert removed."));
    }

    @GetMapping("get")
    @PreAuthorize("permitAll()")
    public AdvertDetailsView getAdvert(@CurrentUser UserPrincipal userPrincipal, @RequestParam Long id) {
        Advert advert = advertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Advert", "id", id));
        if (userPrincipal != null)
            userService.addCategoryEntry(advert.getCategory().getId(), userPrincipal.getId(), 0.01);
        statsService.addAdvertEntry(id);


        return new AdvertDetailsView(advert);
    }

    @GetMapping("/last")
    @PreAuthorize("permitAll()")
    public List<AdvertSummaryView> getLastUserAdverts(
            @CurrentUser UserPrincipal userPrincipal, 
            @RequestParam Long userId, 
            @RequestParam Integer limit) {
        
        return advertService.getAdvertsByUserId(userId, limit);
    }

    @GetMapping("/browse")
    @PreAuthorize("permitAll()")
    public Page<AdvertSummaryView> getAdverts(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam Long categoryId, Pageable pageable,
            @RequestParam(required = false) String titleContains) {

        if (userPrincipal != null) {
            userService.addCategoryEntry(categoryId, userPrincipal.getId(), 0.001);
        }

        return advertService.getPageByCategoryId(categoryId, pageable, titleContains);
    }

    @GetMapping("recommended")
    @PreAuthorize("hasRole('USER')")
    public List<AdvertSummaryView> getRecommendedAdverts(@CurrentUser UserPrincipal userPrincipal,
                                                         @RequestParam Long advertCount) {
        return advertUserService.getRecommendedAdverts(userPrincipal.getId(), advertCount);
    }

    @PostMapping("report")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> reportAdvert(@CurrentUser UserPrincipal userPrincipal,
                                          @Valid @RequestBody ReportAdvertRequest request) {
        reportService.addReport(userPrincipal.getId(), request.getAdvertId(), request.getComment());
        statsService.setAdvertReported(request.getAdvertId());
        return ResponseEntity.ok(new ApiResponse(true, "AdvertReport added"));
    }


    @GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getImage(@RequestParam("advertId") Long advertId) {
        ResponseEntity<?> response;
        try {
            response = ResponseEntity.ok(advertService.loadImage(advertId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            response = ResponseEntity.ok(new ApiResponse(false, e.toString()));
        }

        return response;
    }
}

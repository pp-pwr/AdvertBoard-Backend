package ppztw.AdvertBoard.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ppztw.AdvertBoard.Advert.AdvertUserService;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.*;
import ppztw.AdvertBoard.Payload.Advert.CreateAdvertRequest;
import ppztw.AdvertBoard.Payload.Advert.EditAdvertRequest;
import ppztw.AdvertBoard.Payload.ApiResponse;
import ppztw.AdvertBoard.Repository.AdvertRepository;
import ppztw.AdvertBoard.Repository.SubcategoryRepository;
import ppztw.AdvertBoard.Repository.TagRepository;
import ppztw.AdvertBoard.Repository.UserRepository;
import ppztw.AdvertBoard.Security.CurrentUser;
import ppztw.AdvertBoard.Security.UserPrincipal;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/advert")
public class AdvertController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private AdvertUserService advertUserService;

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private TagRepository tagRepository;

    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addAdvert(@CurrentUser UserPrincipal userPrincipal,
                                       @Valid @RequestPart("advertInfo") CreateAdvertRequest createAdvertRequest,
                                       @Valid @RequestPart("image") MultipartFile image) {

        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userPrincipal.getId()));


        List<Advert> advertList = user.getAdverts();
        if (advertList == null)
            advertList = new ArrayList<Advert>();

        Subcategory subcategory = subcategoryRepository.findBySubcategoryName(
                createAdvertRequest.getSubcategory()).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Subcategory", "name", createAdvertRequest.getSubcategory()));


        List<Tag> tags = new ArrayList<>();
        if (createAdvertRequest.getTags() != null)
            for (String tag : createAdvertRequest.getTags()) {
                Tag tempTag = (tagRepository.findByName(tag).orElseGet(() -> new Tag(tag)));
                tags.add(tempTag);
            }

        Image img = null;
        if (image != null) {
            if (Objects.equals(image.getContentType(), "image/png")) {
                try {
                    img = new Image(image.getName(), image.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        Advert advert = new Advert(
                createAdvertRequest.getTitle(),
                tags,
                createAdvertRequest.getDescription(),
                img, subcategory,
                user);
        advertList.add(advert);
        user.setAdverts(advertList);
        subcategory.addAdvert(advert);

        userRepository.save(user);
        subcategoryRepository.save(subcategory);

        return ResponseEntity.ok(new ApiResponse(true, "Added new advert"));
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> editAdvert(@CurrentUser UserPrincipal userPrincipal,
                                        @Valid @RequestPart EditAdvertRequest editAdvertRequest,
                                        @Valid @RequestPart("image") MultipartFile image) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        Advert advert = advertUserService.findAdvert(user, editAdvertRequest.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Advert", "id", editAdvertRequest.getId()));


        if (image != null) {
            if (Objects.equals(image.getContentType(), "image/png")) {
                Image img = null;
                try {
                    img = new Image(image.getName(), image.getBytes());
                    advert.setImage(img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (editAdvertRequest.getTags() != null) {
            List<Tag> tags = new ArrayList<>();
            for (String tag : editAdvertRequest.getTags()) {
                Tag tempTag = (tagRepository.findByName(tag).orElseGet(() -> new Tag(tag)));
                tags.add(tempTag);
            }
            advert.setTags(tags);
        }
        if (editAdvertRequest.getTitle() != null)
            advert.setTitle(editAdvertRequest.getTitle());

        if (editAdvertRequest.getDescription() != null)
            advert.setDescription(editAdvertRequest.getDescription());

        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse(true, "Advert edited successfully"));
    }

    @PostMapping("remove")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> removeAdvert(@CurrentUser UserPrincipal userPrincipal,
                                          @RequestParam Long id) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        Advert advert = advertUserService.findAdvert(user, id).orElseThrow(() ->
                new ResourceNotFoundException("Advert", "id", id));

        advert.setStatus(Advert.Status.ARCHIVED);
        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse(true, "Advert removed."));
    }

    @GetMapping("all")
    @PreAuthorize("permitAll()")
    public Page<Advert> getAllAdverts(Pageable pageable) {
        return advertRepository.findAll(pageable);
    }

    @GetMapping("get")
    @PreAuthorize("permitAll()")
    public Advert getAdvert(@RequestParam Long id) {
        return advertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Advert", "id", id));
    }
}

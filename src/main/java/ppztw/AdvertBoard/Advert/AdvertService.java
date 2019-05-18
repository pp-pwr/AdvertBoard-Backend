package ppztw.AdvertBoard.Advert;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ppztw.AdvertBoard.Exception.IncorrectFileException;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.Advert.Category;
import ppztw.AdvertBoard.Repository.Advert.AdvertRepository;
import ppztw.AdvertBoard.Repository.Advert.CategoryRepository;
import ppztw.AdvertBoard.View.Advert.AdvertSummaryView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdvertService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AdvertRepository advertRepository;

    public Page<AdvertSummaryView> getPageByCategoryId(Long categoryId, Pageable pageable,
                                                       String titleContains) {
        List<Long> categoryIds = getAllCategoryIds(categoryId);
        List<AdvertSummaryView> summaryViewList = new ArrayList<>();
        Page<Advert> adverts;
        if (titleContains != null && !titleContains.isEmpty())
            adverts = advertRepository.findAllByCategoryIdInAndTitleLike(categoryIds, titleContains, pageable);
        else
            adverts = advertRepository.findAllByCategoryIdIn(categoryIds, pageable);
        for (Advert advert : adverts)
            summaryViewList.add(new AdvertSummaryView(advert));

        return new PageImpl<>(summaryViewList, pageable, adverts.getTotalElements());
    }

    private List<Long> getAllCategoryIds(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(category.getId());
        for (Category subcategory : category.getAllSubcategories())
            categoryIds.add(subcategory.getId());
        return categoryIds;
    }

    public List<AdvertSummaryView> getAdvertsByUserId(Long userId, Integer limit) {
        List<AdvertSummaryView> summaryViewList = new ArrayList<>();
        List<Advert> adverts = advertRepository.findByUserId(userId, limit);
        for (Advert advert : adverts)
            summaryViewList.add(new AdvertSummaryView(advert));

        return summaryViewList;
    }

    public Resource loadImage(Long advertId) throws MalformedURLException {
        Optional<Advert> advert = advertRepository.findById(advertId);
        Path file;
        Resource resource;

        if(advert.isPresent()) {
            file = Paths.get(advert.get().getImagePath());
            resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return new UrlResource(Paths.get("./images/default.jpg").toUri());
            }
        }

        throw new ResourceNotFoundException("Advert", "ID", advertId);
    }
}

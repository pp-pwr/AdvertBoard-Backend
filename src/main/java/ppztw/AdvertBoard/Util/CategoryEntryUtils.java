package ppztw.AdvertBoard.Util;

import ppztw.AdvertBoard.Model.User.User;

import java.util.HashMap;
import java.util.Map;

public class CategoryEntryUtils {
    public static Map<Long, Double> addEntryValue(Long categoryId, User user, Double val) {
        Map<Long, Double> categoryEntries = user.getCategoryEntries();
        if (categoryEntries == null) {
            categoryEntries = new HashMap<Long, Double>();
            categoryEntries.put(categoryId, val);
        } else {
            if (categoryEntries.containsKey(categoryId))
                val = val + categoryEntries.get(categoryId);
            categoryEntries.put(categoryId, val);
        }
        return categoryEntries;
    }
}

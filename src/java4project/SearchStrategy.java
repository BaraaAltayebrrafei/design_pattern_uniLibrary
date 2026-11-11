package java4project;


import java.util.*;
import java.util.List;

public interface SearchStrategy {
    List<Media> search(List<Media> medias, String query);
}

class SearchBySpecialite implements SearchStrategy {

    @Override
    public List<Media> search(List<Media> medias, String specialtyName) {
        List<Media> result = new ArrayList<>();
        for (Media m : medias) {
            for (Matiere mat : m.getRelatedMatiere()) {
                if (mat.getParentSpecialite().getName().equalsIgnoreCase(specialtyName)) {
                    result.add(m);
                    break;
                }
            }
        }
        return result;
    }
}

class SearchByMatiere implements SearchStrategy {

    @Override
    public List<Media> search(List<Media> medias, String matiereCode) {
        List<Media> result = new ArrayList<>();
        for (Media m : medias) {
            for (Matiere mat : m.getRelatedMatiere()) {
                if (mat.getCode().equalsIgnoreCase(matiereCode)) {
                    result.add(m);
                    break;
                }
            }
        }
        return result;
    }
}

class SearchByMediaId implements SearchStrategy {

    @Override
    public List<Media> search(List<Media> medias, String mediaId) {
        List<Media> result = new ArrayList<>();
        for (Media m : medias) {
            if (String.valueOf(m.getId()).equals(mediaId)) {
                result.add(m);
            }
        }
        return result;
    }
}

class SearchByTitle implements SearchStrategy {

    @Override
    public List<Media> search(List<Media> medias, String title) {
        List<Media> result = new ArrayList<>();
        for (Media m : medias) {
            if (m.getTitre().toLowerCase().contains(title.toLowerCase())) {
                result.add(m);
            }
        }
        return result;
    }
}

class SearchContext {

    private SearchStrategy strategy;

    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Media> executeStrategy(List<Media> medias, String query) {
        if (strategy == null) throw new IllegalStateException("No search strategy set");
        return strategy.search(medias, query);
    }
}
////////////////////////How to use it in your GUI or controller/////////////////////////////
//SearchContext context = new SearchContext();
//
//// Example: search by title
//context.setStrategy(new SearchByTitle());
//List<Media> result = context.executeStrategy(mediaRepository.findAll(), "Generics");
//
//// Example: search by specialty
//context.setStrategy(new SearchBySpecialite());
//List<Media> result2 = context.executeStrategy(mediaRepository.findAll(), "Informatique");

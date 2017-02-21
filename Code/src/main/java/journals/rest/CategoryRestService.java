package journals.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journals.model.Category;
import journals.repository.CategoryRepository;

import java.util.List;

@RestController
@RequestMapping("/public/rest/category")
public class CategoryRestService {

    @Autowired
    private CategoryRepository repository;


    @RequestMapping
    public List<Category> getCategories() {
        return repository.findAll();
    }

}

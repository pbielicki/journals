package journals.dto;

import journals.model.Journal;

/**
 * Created by pbielicki on 15/02/2017.
 */
public class JournalDTO {

    private Long categoryId;

    private String name;

    public JournalDTO(Journal journal) {
        this.categoryId = journal.getCategory().getId();
        this.name = journal.getName();
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package dev.kuehni.jeecms.page.data;

import dev.kuehni.jeecms.util.data.CrudRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PageRepository extends CrudRepository<Page, Long> {
    public PageRepository() {
        super(Page.class);
    }
}
package io.zahori.process.pages;

import io.zahori.framework.core.Locator;
import io.zahori.framework.core.Page;
import io.zahori.framework.core.PageElement;

public class WikipediaPage extends Page {

    private static final long serialVersionUID = -8823897590918241825L;

    public WikipediaPage(io.zahori.framework.core.TestContext testContext) {
        super(testContext);
    }

    public void selectLanguage(String language) {
        PageElement languageLink = new PageElement(this, "Language selector", Locator.xpath("//a/strong[contains(text(),\"" + language + "\")]"));
        languageLink.click();
    }

    public void search(String textToSearch) {
        PageElement searchField = new PageElement(this, "Search field", Locator.id("searchInput"));
        searchField.write(textToSearch);

        PageElement searchButton = new PageElement(this, "Search button", Locator.id("searchButton"));
        searchButton.click();
    }

}

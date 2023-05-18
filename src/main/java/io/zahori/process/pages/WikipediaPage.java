package io.zahori.process.pages;

/*-
 * #%L
 * zahori-process
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2021 PANEL SISTEMAS INFORMATICOS,S.L
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
import io.zahori.framework.core.Locator;
import io.zahori.framework.core.Page;
import io.zahori.framework.core.PageElement;

public class WikipediaPage extends Page {

    private static final long serialVersionUID = -8823897590918241825L;

    private PageElement searchField = new PageElement(this, "Search field", Locator.name("search"));
    private PageElement firstParagraph = new PageElement(this, "First paragraph", Locator.xpath("//*[@id='mw-content-text']/div/p[1]"));

    public WikipediaPage(io.zahori.framework.core.TestContext testContext) {
        super(testContext);
    }

    public boolean pageLoaded() {
        PageElement logo = new PageElement(this, "Central logo", Locator.xpath("//img[@class='central-featured-logo']"));
        return logo.waitElementVisible();
    }

    public void selectLanguage(String language) {
        PageElement languageLink = new PageElement(this, "Language selector", Locator.xpath("//a/strong[contains(text(),\"" + language + "\")]"));
        languageLink.click();

        PageElement mainContent = new PageElement(this, "Main content", Locator.xpath("//*[@id='content']"));
        mainContent.waitElementVisible();
    }

    public void search(String textToSearch) {
        if (!searchField.isVisibleWithoutWait()) {
            PageElement searchIcon = new PageElement(this, "Search icon", Locator.xpath("//*[@id='p-search']/a"));
            searchIcon.click();
        }

        searchField.write(textToSearch);

        PageElement searchButton = new PageElement(this, "Search button", Locator.xpath("//form[@id='searchform']//button"));
        searchButton.click();

        firstParagraph.waitElementVisible();
    }

    public String getFirstParagraph() {
        return firstParagraph.getText();
    }

}

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

public class BingPage extends Page {

    private static final long serialVersionUID = -8823897590918241825L;

    public BingPage(io.zahori.framework.core.TestContext testContext) {
        super(testContext);
    }

    public void acceptCookies() {
        PageElement acceptCookies = new PageElement(this, "Accept cookies", Locator.id("bnp_btn_accept"));
        if (acceptCookies.isVisible()) {
            acceptCookies.click();
            testContext.logStepPassedWithScreenshot("Accept cookies");
        }
    }

    public void search(String textToSearch) {
        PageElement searchField = new PageElement(this, "Search field", Locator.id("sb_form_q"));
        searchField.write(textToSearch);

        PageElement searchButton = new PageElement(this, "Search button", Locator.xpath("//label[@class='search icon tooltip']"));
        searchButton.click();
    }

}

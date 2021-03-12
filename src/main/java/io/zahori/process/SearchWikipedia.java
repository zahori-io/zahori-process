package io.zahori.process;

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

import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import io.zahori.framework.core.TestContext;
import io.zahori.model.process.CaseExecution;
import io.zahori.process.framework.ZahoriProcess;
import io.zahori.process.pages.WikipediaPage;

@RestController
public class SearchWikipedia extends ZahoriProcess {

    /*
     * Warning! Do not declare any variables here, values are overwritten when
     * several cases are executed in parallel.
     */

    @Override
    public void run(TestContext testContext, CaseExecution caseExecution) {

        // Read case data
        Map<String, String> data = caseExecution.getCas().getDataMap();
        String language = data.get("Language");
        String searchText = data.get("Search");

        // Load page
        testContext.getBrowser().loadPage("https://www.wikipedia.org/");

        WikipediaPage wiki = new WikipediaPage(testContext);
        if (wiki.pageLoaded()) {
            testContext.logStepPassedWithScreenshot("Page loaded");
        }

        wiki.selectLanguage(language);
        testContext.logStepPassedWithScreenshot("Language selected: {0}", language);

        wiki.search(searchText);
        testContext.logStepPassedWithScreenshot("Search: {0}", searchText);

        String firstParagraph = wiki.getFirstParagraph();
        testContext.logStepPassed("First paragraph: {0}", firstParagraph);
    }
}

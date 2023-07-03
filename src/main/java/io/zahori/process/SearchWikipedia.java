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
import io.zahori.framework.core.TestContext;
import io.zahori.framework.core.ZahoriProcess;
import io.zahori.model.process.CaseExecution;
import io.zahori.process.pages.WikipediaPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.web.bind.annotation.RestController;

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
        String url = caseExecution.getConfiguration().getEnvironmentUrl();
        testContext.getBrowser().loadPage(url);

        WikipediaPage wiki = new WikipediaPage(testContext);
        if (wiki.pageLoaded()) {
            testContext.logStepPassedWithScreenshot("Page loaded");
        }

        wiki.selectLanguage(language);
        testContext.logStepPassedWithScreenshot("Language selected: {}", language);

        wiki.search(searchText);
        testContext.logStepPassedWithScreenshot("Search: {}", searchText);

        String firstParagraph = wiki.getFirstParagraph();
        testContext.logStepPassed("First paragraph: {}", firstParagraph);
        
        RemoteWebDriver driver = (RemoteWebDriver) testContext.driver;

        Path printPage = Paths.get(testContext.getEvidencesFolder() + "PrintPage.pdf");
        Pdf print = driver.print(new PrintOptions());
        try {
            Files.write(printPage, OutputType.BYTES.convertFromBase64Png(print.getContent()));
        } catch (IOException ex) {
            testContext.logStepPassedWithScreenshot("Error printing page to pdf: {}", ex.getMessage());
        }
        testContext.addAttachment(printPage.toString());
        
    }
}

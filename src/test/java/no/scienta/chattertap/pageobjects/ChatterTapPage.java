package no.scienta.chattertap.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ChatterTapPage {
  private final WebDriver browser;

  public ChatterTapPage(WebDriver browser, String pageUrl) {
    this.browser = browser;
    this.browser.get(pageUrl);
  }

  public void searchFor(String placeTerm) {
    WebElement searchInput = browser.findElement(By.name("place"));
    searchInput.clear();
    searchInput.sendKeys(placeTerm);
    searchInput.submit();
  }

  public List<WebElement> getPlaces() {
    return browser.findElements(By.cssSelector("#places li"));
  }

  public boolean hasNoHitsMessage() {
    return !browser.findElements(By.cssSelector("#places .empty")).isEmpty();
  }

  public boolean hasErrorMessage() {
    return !browser.findElements(By.cssSelector("#places .error")).isEmpty();
  }
}

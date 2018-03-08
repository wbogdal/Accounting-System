package pl.coderstrust.db.impl.memory.SQL.Company;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Company;

import java.util.List;


@RestController
public class CompanyController {

  private CompanyService companyService;

  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }

  @CrossOrigin
  @RequestMapping(path = "/company", method = RequestMethod.GET)
  public List<Company> getCompanies() {
    return companyService.getCompanies();
  }

  @CrossOrigin
  @RequestMapping(path = "/company/{id}", method = RequestMethod.GET)
  public Company getCompanyByVatIdentificationNumber(@PathVariable String id) {
    return companyService.getCompanyByVatIdentificationNumber(id);
  }

  @CrossOrigin
  @RequestMapping(path = "/company/delete/{id}", method = RequestMethod.DELETE)
  public void deleteCompanyById(@PathVariable String id) {
    companyService.deleteByVatIdentificationNumber(id);
  }

  @CrossOrigin
  @RequestMapping(path = "/companyName/{name}", method = RequestMethod.GET)
  public Company getCompanyByName(@PathVariable String name) {
    return companyService.getCompanyByName(name);
  }

  @CrossOrigin
  @RequestMapping(path = "/companyName/delete/{name}", method = RequestMethod.DELETE)
  public void deleteCompanyByName(@PathVariable String name) {
    companyService.deleteByName(name);
  }

  @CrossOrigin
  @RequestMapping(value = "company/update",
      params = {"id", "name"},
      method = RequestMethod.PUT)
  public void updateCompanyName(@RequestParam("id") String id, @RequestParam("name")
      String name) {
    companyService.updateCompany(id, name);
  }

  @CrossOrigin
  @RequestMapping(value = "company/insert",
      params = {"id", "name"},
      method = RequestMethod.POST)
  public void insertCompany(@RequestParam("id") String id, @RequestParam("name")
      String name) {
    companyService.insertCompany(id, name);
  }
}

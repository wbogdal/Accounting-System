package pl.coderstrust.db.impl.memory.SQL.Company;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import pl.coderstrust.model.Company;

@Service
public class CompanyService {

  @Resource
  private CompanyRepository companyRepository;

  public CompanyService(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  public List<Company> getCompanies() {
    return companyRepository.findAll();
  }

  public Company getCompanyByVatIdentificationNumber(String id) {
    return companyRepository.getCompanyByVatIdentificationNumber(id);
  }

  public void deleteByVatIdentificationNumber(String id) {
    companyRepository.deleteCompanyByVatIdentificationNumber(id);
  }

  public Company getCompanyByName(String name) {
    return companyRepository.getCompanyByName(name);
  }

  public void deleteByName(String name) {
    companyRepository.deleteCompanyByName(name);
  }

  public void insertCompany(String vatNumber, String name) {
    Company company = new Company();
    company.setVatIdentificationNumber(vatNumber);
    company.setName(name);
    companyRepository.save(company);
  }

  public void updateCompany(String id, String name) {
    Iterator<Company> companyIterator = companyRepository.findAll().iterator();
    while (companyIterator.hasNext()) {
      Company current = companyIterator.next();
      if (current.getVatIdentificationNumber().equals(id)) {
        current.setName(name);
        companyRepository.save(current);
      }
    }
  }

}

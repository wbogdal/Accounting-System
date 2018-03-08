package pl.coderstrust.db.impl.memory.SQL.Company;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.coderstrust.model.Company;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyServiceTest {

  @Autowired
  CompanyRepository companyRepository;

  @Test
  public void shouldInsertCompany() throws Exception {

    //given
    CompanyService companyService = new CompanyService(companyRepository);
    String name = "Yellow";
    String vatNumber = "PL23456";

    //when
    companyService.insertCompany(vatNumber, name);

    //then
    Assert.assertNotNull(companyRepository.getCompanyByName("Yellow"));
  }

  @Test
  public void shouldGetCompanies() throws Exception {
    //given
    CompanyService companyService = new CompanyService(companyRepository);
    Company company = new Company();
    company.setName("Blue");
    company.setVatIdentificationNumber("PL123456");

    //when
    companyRepository.save(company);

    //then
    assertTrue(companyService.getCompanies() != null);

  }

  @Test
  public void shouldGetCompanyByVatIdentificationNumber() throws Exception {
    //given
    CompanyService companyService = new CompanyService(companyRepository);
    Company company = new Company();
    company.setName("Blue");
    company.setVatIdentificationNumber("PL1234567");

    //when
    companyRepository.save(company);

    //then
    Assert.assertNotNull(companyService.getCompanyByVatIdentificationNumber("PL1234567"));

  }

  @Test
  public void shouldDeleteByVatIdentificationNumber() throws Exception {
    //given
    CompanyService companyService = new CompanyService(companyRepository);
    Company company = new Company();
    company.setName("Blue");
    company.setVatIdentificationNumber("PL12345678");
    companyRepository.save(company);

    //when
    companyService.deleteByVatIdentificationNumber("PL12345678");

    //then
    Assert.assertNull(companyService.getCompanyByVatIdentificationNumber("PL12345678"));
  }

  @Test
  public void ShouldDeleteByName() throws Exception {
    //given
    CompanyService companyService = new CompanyService(companyRepository);
    Company company = new Company();
    company.setName("White");
    company.setVatIdentificationNumber("PL123456789");
    companyRepository.save(company);

    //when
    companyService.deleteByName("White");

    //then
    Assert.assertNull(companyService.getCompanyByVatIdentificationNumber("PL123456789"));
  }

  @Test
  public void ShouldGetCompanyByName() throws Exception {
    //given
    CompanyService companyService = new CompanyService(companyRepository);
    Company company = new Company();
    company.setName("Pink");
    company.setVatIdentificationNumber("PL00561234567");

    //when
    companyRepository.save(company);

    //then
    Assert.assertNotNull(companyService.getCompanyByName("Pink"));
  }

  @Test
  public void ShouldUpdateCompany() throws Exception {
    //given
    CompanyService companyService = new CompanyService(companyRepository);
    Company company = new Company();
    company.setName("Blue");
    company.setVatIdentificationNumber("PL6789");
    companyRepository.save(company);

    //when
    companyService.updateCompany("PL6789", "Black");

    //then
    Assert.assertTrue(companyService.getCompanyByVatIdentificationNumber("PL6789")
        .getName() == "Black");
  }
}
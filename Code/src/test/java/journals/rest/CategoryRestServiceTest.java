package journals.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.List;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import journals.WebApplication;
import journals.model.Category;
import journals.repository.CategoryRepository;

@SpringApplicationConfiguration(classes = WebApplication.class)
@TestPropertySource(locations="classpath:test.properties")  
@WebAppConfiguration
@ActiveProfiles("web")
public class CategoryRestServiceTest {

  @ClassRule
  public static final SpringClassRule SCR = new SpringClassRule();

  @Rule
  public final SpringMethodRule springMethodRule = new SpringMethodRule();
  
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private CategoryRepository categoryRepository;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	public void getCategories() throws Exception {
		ResultActions test = mockMvc.perform(get("/public/rest/category")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType));

		List<Category> categories = categoryRepository.findAll();
		for (int i = 0; i < categories.size(); i++) {
			test.andExpect(jsonPath("$[" + i + "].id", is(categories.get(i).getId().intValue())))
				.andExpect(jsonPath("$[" + i + "].name", is(categories.get(i).getName())));
		}
	}
}

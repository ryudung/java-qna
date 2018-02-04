package codesquad.web;

import codesquad.UnAuthorizedException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.helper.HtmlFormDataBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class QuestionAcceptanceTest extends AcceptanceTest {

    @Autowired
    QuestionRepository questionRepository;

    @Test
    public void write_success(){
        User loginUser = defaultUser();

        HttpEntity<MultiValueMap<String, Object>> request
                = HtmlFormDataBuilder
                .urlEncodedForm()
                .addParameter("title", "aaaa")
                .addParameter("contents", "aaa")
                .build();

        ResponseEntity<String> response = basicAuthTemplate(loginUser)
                                            .postForEntity("/qna/write", request, String.class);

        Question question = questionRepository.findOne(3L);

        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertNotNull(question);
        assertThat(question.getTitle(), is("aaaa"));
        assertThat(question.getContents(), is("aaa"));
        assertThat(response.getHeaders().getLocation().getPath(), is("/"));
    }

    @Test
    public void update_login(){
        User loginUser = defaultUser();

        HttpEntity<MultiValueMap<String, Object>> request
                = HtmlFormDataBuilder
                .urlEncodedForm()
                .addParameter("title", "bbbb")
                .addParameter("contents", "bbb")
                .build();

        ResponseEntity<String> response = basicAuthTemplate(loginUser)
                .postForEntity(String.format("/qna/update/%d", 1), request, String.class);

        Question question = questionRepository.findOne(1L);
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));

        assertNotNull(question);
        assertThat(question.getTitle(), is("bbbb"));
        assertThat(question.getContents(), is("bbb"));
        assertThat(response.getHeaders().getLocation().getPath(), is("/"));
    }


    @Test
    public void update_no_login() throws UnAuthorizedException{
        User loginUser = defaultUser();

        HttpEntity<MultiValueMap<String, Object>> request
                = HtmlFormDataBuilder
                .urlEncodedForm()
                .addParameter("title", "bbbb")
                .addParameter("contents", "bbb")
                .build();

        ResponseEntity<String> response=basicAuthTemplate(loginUser)
                .postForEntity(String.format("/qna/update/%d", 2), request, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void delete(){
        User loginUser = defaultUser();

        ResponseEntity<String> response
                = basicAuthTemplate(loginUser)
                .getForEntity(String.format("/qna/delete/%d", 1), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertNull(questionRepository.findOne(1L));
    }
}

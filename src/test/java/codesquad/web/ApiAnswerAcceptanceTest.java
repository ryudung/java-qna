package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest{
    private static final String DEFAULT_LOGIN_USER = "javajigi";

    @Test
    public void add() {

        User loginUser = findByUserId(DEFAULT_LOGIN_USER);

        String contents = "aaaaaa";

        String location = this.createResource("/api/answers/"+ 1, contents, loginUser);

        Answer answer = this.getResource(location, Answer.class, loginUser);

        assertThat(answer.getContents().equals(contents), is(true));
    }

    @Test
    public void update() {

        User loginUser = findByUserId(DEFAULT_LOGIN_USER);

        String contents = "aaaaaa";

        String location = this.createResource("/api/answers/"+ 1, contents, loginUser);

        contents = "bbbbb";

        basicAuthTemplate(loginUser).put(location, contents);

        Answer answer = this.getResource(location, Answer.class, loginUser);

        assertThat(answer.getContents().equals(contents), is(true));

    }

    protected String createResource(String path, Object bodyPayload, User loginUser) {
        ResponseEntity<String> response = basicAuthTemplate(loginUser).postForEntity(path, bodyPayload, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        return response.getHeaders().getLocation().getPath();
    }

    protected <T> T getResource(String location, Class<T> responseType, User loginUser) {
        return basicAuthTemplate(loginUser).getForObject(location, responseType);
    }
}

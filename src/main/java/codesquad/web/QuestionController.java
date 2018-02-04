package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.UnAuthenticationException;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/qna")
public class QuestionController {

    @Autowired
    private QnaService qnaService;

    @GetMapping("/{id}")
    public String findDetail(
            Model model
            , @PathVariable Long id){
        model.addAttribute("question", qnaService.findById(id));
        return "qna/show";
    }

    @GetMapping("/writeForm")
    public String write_form(){
        return "qna/form";
    }

    @PostMapping("/write")
    public String write(
            @LoginUser User user
            , QuestionDto questionDto){
        qnaService.create(user, questionDto);
        return "redirect:/";
    }

    @GetMapping("/updateForm/{id}")
    public String update_form(Model model
            , @PathVariable("id") long id){
        model.addAttribute("question", qnaService.findById(id));
        return "qna/updateForm";
    }

    @PostMapping("/update/{id}")
    public String update(@LoginUser User user
            , @PathVariable("id") long id
            , QuestionDto questionDto) throws UnAuthenticationException {

        qnaService.update(user, id, questionDto);

        return "redirect:/";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(
            @LoginUser User user
            , @PathVariable("id") long id) throws CannotDeleteException {

        qnaService.deleteQuestion(user, id);

        return "redirect:/";
    }

}

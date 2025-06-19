package uz.zafar.onlinecourse.rest;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zafar.onlinecourse.dto.ResponseDto;
import uz.zafar.onlinecourse.dto.form.LoginForm;
import uz.zafar.onlinecourse.dto.user_dto.req.SignUpForm;
import uz.zafar.onlinecourse.service.AuthService;
import uz.zafar.onlinecourse.service.EmailService;
import uz.zafar.onlinecourse.service.UserService;

import java.io.File;
import java.util.List;
//import uz.farobiy.lesson_11_backend.dto.form.LoginForm;
//import uz.farobiy.lesson_11_backend.service.UserService;

@RestController
@RequestMapping("api/auth")
@Tag(
        name = "Authentication Controller",
        description = "Foydalanuvchini ro‘yxatdan o‘tkazish, tizimga kirish (login) va autentifikatsiya bilan bog‘liq endpointlar"
)
public class AuthRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Value("${homework.video.base.url}")
    private String homeworkVideoBaseUrl;
    @Autowired
    private EmailService emailService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginForm form) throws Exception {
        return ResponseEntity.ok(userService.signIn(form));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpForm form) throws Exception {
        return ResponseEntity.ok(userService.signUp(form));
    }

    @PostMapping("/verification")
    public ResponseEntity<?> getVerificationCode(@RequestParam String verificationCode) throws Exception {
        return ResponseEntity.ok(userService.verifyCode(verificationCode));
    }


    @Hidden
    @GetMapping("send-email")
    public ResponseEntity<?> sendEmail() throws Exception {
        List<String> emails = List.of(
                "info@hdec.kr",
                "contact@samsungengineering.com",
                "hr@skec.com",
                "poscohr@poscoenc.com",
                "gs_hr@gsenc.com",
                "contact@daewooenc.com",
                "hr@lottecon.co.kr",
                "hr@hanwha.com",
                "info@doosan.com",
                "contact@hyundai-rotem.co.kr",
                "posco@posco.com",
                "info@hyundai-steel.com",
                "contact@lg.com",
                "hr@skhynix.com",
                "contact@samsung.com",
                "info@dongkuk.com",
                "recruit@seah.co.kr",
                "iljin@iljin.co.kr",
                "contact@kumkang.com",
                "contact@jobploy.kr",
                "recruit@kolonglobal.com",
                "hr@halla.com",
                "hoban@hoban.co.kr",
                "recruit@ssyenc.com",
                "info@dlenc.co.kr",
                "info@krcon.co.kr",
                "bumyang@bumyang.co.kr",
                "contact@samoo.com",
                "recruit@daelim.co.kr",
                "recruit@hanshin.com",
                "hobanindustry@hoban.co.kr",
                "korea@jac-recruitment.kr",
                "info@hrnetone.com",
                "info@energyresourcing.com",
                "recruit@nesco.co.kr",
                "samjin@samjinp.com",
                "shenc@shenc.co.kr",
                "recruit@dongbu.co.kr",
                "recruit@hdc.co.kr",
                "hr@daewooenc.com",
                "recruit@poscoict.com",
                "recruit@dsme.co.kr",
                "recruit@lotteenc.com",
                "contact@hanjin.com",
                "recruit@kepco.co.kr",
                "recruit@doosanenc.com",
                "hdec@hdec.kr",
                "recruit@samsungcnt.com",
                "recruit@poscoplant.com",
                "hr@hanwha-techwin.com",
                "recruit@kolonindustries.com",
                "hr@skinnovation.com",
                "recruit@gspower.com",
                "contact@kumhoind.com",
                "recruit@hanwha.co.kr",
                "recruit@hyundai-steel.com",
                "recruit@lgchem.com",
                "recruit@poscointl.com",
                "hr@samsungshi.com",
                "info@doosanbobcat.kr",
                "recruit@lignex1.com",
                "recruit@hyundaimobis.com",
                "recruit@hanwhaaero.com",
                "contact@kumkangkind.com",
                "recruit@samsungengineering.com",
                "recruit@koreazinc.com",
                "recruit@hanwhadefense.com",
                "hr@skmaterials.com",
                "recruit@lsi.com",
                "recruit@hhi.co.kr",
                "recruit@daewoo.com",
                "recruit@soil.com",
                "recruit@kmp.co.kr",
                "recruit@hanwhaenergy.com",
                "recruit@lge.com",
                "recruit@sktelecom.com",
                "recruit@poscoenc.com",
                "recruit@hyundaieng.com",
                "recruit@hdc.co.kr",
                "recruit@poscoict.com",
                "recruit@hydoboilbank.com",
                "recruit@gsglobal.com",
                "recruit@hanwhasolutions.com",
                "hr@doosan.com",
                "recruit@skens.com",
                "recruit@lscns.com",
                "recruit@samsungsdi.com",
                "recruit@hyosung.com",
                "recruit@kumhochem.com",
                "recruit@hhi-greenenergy.com",
                "recruit@daelimchemical.com",
                "recruit@hanwhamarine.com",
                "recruit@oci.co.kr",
                "recruit@kogas.or.kr",
                "recruit@khnp.co.kr",
                "recruit@doosanheavy.com",
                "recruit@hyundai-rotem.com",
                "recruit@samyoungenc.com",
                "recruit@dongbu.com",
                "recruit@daesang.com",
                "recruit@cj.co.kr",
                "recruit@kt.com",
                "recruit@kolonindustries.com",
                "recruit@lottechem.com",
                "recruit@hanshin.com",
                "recruit@samoo.com",
                "recruit@halla.com",
                "recruit@hyosungheavy.com",
                "recruit@poscoenergy.com",
                "recruit@kolonenc.com",
                "recruit@dsme.co.kr",
                "recruit@lguplus.co.kr",
                "recruit@skhynix.com",
                "recruit@samsungsds.com",
                "recruit@hyundaiglvs.com",
                "recruit@doosanbobcat.com",
                "recruit@korail.com",
                "recruit@samsungfire.com",
                "recruit@hanwhalife.com",
                "recruit@gsretail.com",
                "recruit@lotteshopping.com",
                "recruit@skholdings.com",
                "recruit@cjlogistics.com",
                "recruit@hyundaicard.com",
                "recruit@nhis.com",
                "recruit@miraeasset.com",
                "recruit@kepcoenc.com",
                "recruit@kai.com",
                "recruit@lsmtron.com",
                "recruit@samsungbiologics.com",
                "recruit@skbioscience.com",
                "recruit@lghnh.com",
                "recruit@poscodw.com",
                "recruit@cjenm.com",
                "recruit@hanwhaqcells.com",
                "recruit@doosanfuelcell.com",
                "recruit@ksp.co.kr",
                "recruit@samsungsem.com"
        );

        for (String email : emails) {
            try {
                emailService.sendSimpleMessage(email, "현장 및 생산직 구직 지원 - 마블라노브 바흐티요르 (D-10 비자 소지자)", """
                        안녕하세요.
                        
                        저는 우즈베키스탄 출신의 마블라노브 바흐티요르라고 합니다.
                        
                        현재 D-10 구직 비자로 한국에 체류 중이며, 2023년부터 금속가공 공장과 건설 현장에서 다양한 실무 경험을 쌓았습니다. \s
                        그라인더 작업, 절단 및 용접 기술, 방수(우레탄, 시트), 콘크리트 타설(알폼), 철근 배근, 미장 등 여러 분야의 작업에 능숙합니다. \s
                        또한, 한국어 읽기와 쓰기가 가능하며, 매일 어휘와 표현을 익히며 실력을 향상시키고 있습니다.
                        
                        귀사의 현장직 또는 생산직 채용에 지원하고자 이렇게 이메일을 드립니다. \s
                        제 이력서와 자기소개서를 첨부하였으니 검토해주시면 감사하겠습니다. \s
                        면접 기회를 주신다면 성실히 일하고 회사에 기여하겠습니다.
                        
                        감사합니다.
                        
                        마블라노브 바흐티요르 드림 \s
                        전화번호: 010-7901-8351 \s
                        이메일: mavlonovbaxtiyor1983@gmail.com""");

                File file1 = new File("uploads/files/이력서_Baxtiyor_Mavlonov.docx");
                File file2 = new File("uploads/files/자기소개서_Baxtiyor_Mavlonov (1).docx");

                // Fayl mavjudligini tekshirish
                if (!file1.exists() || !file1.isFile()) {
                    System.err.println("Xato: Fayl topilmadi yoki noto'g'ri: " + file1.getAbsolutePath());
                    continue; // Keyingi emailga o'tish
                }
                if (!file2.exists() || !file2.isFile()) {
                    System.err.println("Xato: Fayl topilmadi yoki noto'g'ri: " + file2.getAbsolutePath());
                    continue;
                }

                System.out.println("Fayl 1 manzili: " + file1.getAbsolutePath());
                System.out.println("Fayl 2 manzili: " + file2.getAbsolutePath());

                // Fayllarni yuborish
                ResponseDto<Void> response1 = emailService.sendFile(
                        email,
                        "현장 및 생산직 구직 지원 - 마블라노브 바흐티요르 (D-10 비자 소지자)",
                        file1);
                if (!response1.isSuccess()) {
                    System.err.println("Fayl 1 yuborishda xatolik: " + response1.getMessage());
                }

                ResponseDto<Void> response2 = emailService.sendFile(
                        email,
                        "현장 및 생산직 구직 지원 - 마블라노브 바흐티요르 (D-10 비자 소지자)",
                        file2);
                if (!response2.isSuccess()) {
                    System.err.println("Fayl 2 yuborishda xatolik: " + response2.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok("success");
    }
}

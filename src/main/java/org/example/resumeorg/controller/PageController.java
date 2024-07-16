package org.example.resumeorg.controller;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.resumeorg.entity.Education;
import org.example.resumeorg.entity.Experience;
import org.example.resumeorg.entity.Technology;
import org.example.resumeorg.entity.User;
import org.example.resumeorg.repo.TechnologyRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.StringJoiner;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/resume")
public class PageController {
    private final TechnologyRepo technologyRepo;


    @GetMapping()
    public String get(Model model, @RequestBody(required = false) User user , HttpSession session,HttpServletResponse response) throws IOException {
        model.addAttribute("technologies", technologyRepo.findAll());
        return "resume";
    }

    @PostMapping()
    public ResponseEntity<?> post(@RequestBody User user, HttpSession session) throws IOException {
        session.setAttribute("user",user);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("generate-pdf")
    public void generatePdf(HttpServletResponse response,HttpSession session) throws IOException {
        byte[] photo= (byte[])session.getAttribute("photo");
        User user = (User)session.getAttribute("user");
        generatePdf(user,photo,response);
        System.out.println(user);
    }

    @GetMapping("getTech")
    public ResponseEntity<?> getTech(@RequestParam Integer id){
        Technology tech = technologyRepo.findById(id).get();
        return ResponseEntity.ok(tech);
    }


    @SneakyThrows
    @PostMapping("savePhoto")
    public ResponseEntity<?> savePhoto(HttpSession session,@RequestParam("photo") MultipartFile photo){
        session.setAttribute("photo",photo.getBytes());
        return ResponseEntity.ok("ok");
    }

    public static void generatePdf(User user, byte[] photo, HttpServletResponse response) throws IOException {
        if (photo==null){
            System.out.println("NULLLLLLL");
        }
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=receipt.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        document.add(new Paragraph("R E S U M E ")
                .setBold()
                .setFontSize(20)
                .setMarginBottom(10));

        ImageData imageData = ImageDataFactory.create(photo);
        Image img = new Image(imageData).setWidth(UnitValue.createPercentValue(30));

        float[] columnWidth = {1, 2};
        Table table = new Table(columnWidth);
        table.setWidth(UnitValue.createPercentValue(100));
        String info = String.format("""
                    First name: %s
                    Last name: %s
                    Father name: %s
                    Age: %d
                    Phone number: %s
                    """, user.getFirstName(), user.getLastName(), user.getFatherName(), user.getAge(), user.getPhoneNumber());

        table.addCell(new Paragraph("Photo"));
        table.addCell(img);
        table.addCell(new Paragraph("Info"));
        table.addCell(new Paragraph(info));

        document.add(table);

        document.add(new Paragraph("Technologies").setFontSize(20));
        StringJoiner techs = new StringJoiner(", ");
        for (Technology technology : user.getTechnologies()) {
            techs.add(technology.getName());
        }
        document.add(new Paragraph(techs.toString()));

        document.add(new Paragraph("Educations").setFontSize(20));
        for (Education education : user.getEducations()) {
            document.add(new Paragraph(education.getName() + ": From " + education.getFroms().toString() + " To: " + education.getTos().toString()));
        }

        document.add(new Paragraph("Experiences").setFontSize(20));
        for (Experience experience : user.getExperiences()) {
            document.add(new Paragraph(experience.getName() + ": From " + experience.getFroms().toString() + " To: " + experience.getTos().toString()));
        }

        document.close();
        writer.flush();
        writer.close();
    }
}

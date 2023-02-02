package com.example.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;

@Route
public class MainView extends VerticalLayout {

    public MainView() {
        var brbPrompt = new H4("Enter number of BRBs:");
        var brbInput = new TextField();
        var brbSubmit = new Button("Submit");
        add(brbPrompt);
        add(new HorizontalLayout(brbInput, brbSubmit));

        int now = LocalDate.now().getDayOfYear();
        double perSem = Math.round(((129.0 - now) / 106.0) * 100.0);
        String perSemString = perSem + "%";
        var semStatement = new Paragraph(perSemString + " of the semester is left.");

        var progSem = new ProgressBar();
        progSem.setMax(400);
        progSem.setMin(0);
        progSem.setValue(perSem * 4.0);

        var progBrb = new ProgressBar();
        progBrb.setMax(400);
        progBrb.setMin(0);

        brbSubmit.addClickListener(e -> {
            removeAll();

            double numBrbs = (double) Integer.parseInt(brbInput.getValue());
            double perBrbs = (numBrbs / 400.0) * 100.0;
            String perBrbString = perBrbs + "%";
            var brbStatement = new Paragraph(
                    "You have " + perBrbString + " of your BRBs left.");
            add(brbStatement);
            progBrb.setValue(numBrbs);

            if (perBrbs >= perSem) {
                progBrb.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);
            } else {
                progBrb.addThemeVariants(ProgressBarVariant.LUMO_ERROR);
            }
            add(progBrb);
            add(semStatement);
            add(progSem);

            if (perBrbs >= perSem + 5) {
                int extraBrbs = (int) ((perBrbs - perSem) * 4);
                add(new Paragraph("You are ahead of schedule! You have "
                        + extraBrbs + " excess BRBs!"));
            } else if (perBrbs >= perSem && perBrbs <= perSem + 5) {
                add(new Paragraph("You are on schedule! Keep spending at the same rate!"));
            } else if (perSem - 5 <= perBrbs) {
                add(new Paragraph("You are slightly behind schedule, you may want to slow "
                        + "down your spending."));
            } else {
                int daysLeft = (int) (129 - ((106 * numBrbs) / 400)) + 1 - now;
                add(new Paragraph("You are behind schedule. To get back on schedule, wait "
                        + daysLeft + " days before spending BRBs."));
            }
        });
    }
}

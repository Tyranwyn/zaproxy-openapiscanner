package org.zaproxy.zap.extension.openapiscanner;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.AbstractDialog;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ImportOpenApiDialog extends AbstractDialog {

    private static final long serialVersionUID = 1L;
    private static final String PREFIX = "openapiscanner.importdialog.";

    private JButton buttonNext = new JButton(Constant.messages.getString(PREFIX + "import"));
    private JButton buttonCancel = new JButton(Constant.messages.getString("all.button.cancel"));
    private JButton buttonFileChooser = new JButton(Constant.messages.getString(PREFIX + "file"));

    private JTextField importUrl = new JTextField(30);

    ExtensionOpenApiScanner caller;

    public ImportOpenApiDialog(JFrame parent, ExtensionOpenApiScanner caller) {
        super(parent, true);
        this.setTitle(Constant.messages.getString(PREFIX + "title"));
        this.caller = caller;

        if (parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }

        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        // Configuring buttons
        buttonFileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onFileButton(); }
        });

        buttonNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onImportButton(); }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onCancelButton(); }
        });

        // Adding components to the frame
        // URL Label
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(new JLabel(Constant.messages.getString(PREFIX + "urllabel")), constraints);

        // URL Field
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.gridwidth = 2;
        add(importUrl, constraints);

        // Import Local File Button
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        add(buttonFileChooser, constraints);

        // Cancel button
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        add(buttonCancel, constraints);

        // Import from url button
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        add(buttonNext, constraints);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void onFileButton() {
        // Prompt for a OpenApi file.
        final JFileChooser chooser = new JFileChooser(Model.getSingleton().getOptionsParam().getUserDirectory());
        int rc = chooser.showOpenDialog(View.getSingleton().getMainFrame());
        if (rc == JFileChooser.APPROVE_OPTION) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ScanOpenApiDialog(View.getSingleton().getMainFrame(), caller, chooser.getSelectedFile());
                }
            });
            setVisible(false);
            dispose();
        }
    }

    private void onImportButton() {
        // Opening scanner dialog
        String url = importUrl.getText();
        // Check if the parameter has any value
        if (!url.isEmpty()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ScanOpenApiDialog(View.getSingleton().getMainFrame(), caller, importUrl.getText());
                }
            });
            setVisible(false);
            dispose();
        }
    }

    private void onCancelButton() {
        ImportOpenApiDialog.this.setVisible(false);
        ImportOpenApiDialog.this.dispose();
    }
}

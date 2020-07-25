package com.example.onlinecvmaker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.onlinecvmaker.DBRelatedCode.DBHelper;
import com.example.onlinecvmaker.dataModels.ContactDetails;
import com.example.onlinecvmaker.dataModels.Experience;
import com.example.onlinecvmaker.dataModels.Projects;
import com.example.onlinecvmaker.dataModels.Qualification;
import com.example.onlinecvmaker.dataModels.Reference;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CVCreatingActivity extends AppCompatActivity {
    MyCV personCV;
    SQLiteDatabase db;

    Font headingsFont, normalFont, listFont;
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cvcreating);

        personCV = (MyCV) getApplication();

        Toast.makeText(this, "cv name "+MyCV.CVname, Toast.LENGTH_LONG).show();
    }

    public void btnClick(View view){
        switch(view.getId()){
            case R.id.personalInfoBtn:
                Toast.makeText(getApplicationContext(), "Person btn clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CVCreatingActivity.this, PersonalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.objectiveBtn:
                startActivity(new Intent(CVCreatingActivity.this, ObjectiveActivity.class));
                break;
            case R.id.qualificationBtn:
                Toast.makeText(getApplicationContext(), "Qualification btn clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CVCreatingActivity.this, QualificationActivity.class));
                break;
            case R.id.workBtn:
                startActivity(new Intent(CVCreatingActivity.this, ExperienceActivity.class));
                break;
            case R.id.contactBtn:
                try {
                    startActivity(new Intent(CVCreatingActivity.this, ContactActivity.class));
                }
                catch (Exception ex){
                    Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.projectBtn:
                startActivity(new Intent(CVCreatingActivity.this, ProjectsActivity.class));
                break;
            case R.id.referenceBtn:
                startActivity(new Intent(CVCreatingActivity.this, ReferenceActivity.class));
                break;
            case R.id.otherBtn:
                startActivity(new Intent(CVCreatingActivity.this, OtherActivity.class));
                break;
            case R.id.saveCV:
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    //check for permission enabled or not
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //if permission not granted or denied then request it
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, 10);
                    }else{
                        saveCV();
                    }
                }else{
                    saveCV();
                }
                //saveCV();
                break;
            case R.id.exportBtn:
                exportPupupWindow();
                break;
            case R.id.previewBtn:
                openGeneratedPDF();
                break;
        }
    }

    private void saveCV() {

        savePdftoStoreage();
        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        dbHelper.saveCVtoDB(db, personCV, this);
        db.close();
    }

    private void savePdftoStoreage(){
        //saveCV();
        Document document = new Document(PageSize.A4);
        String filePath = Environment.getExternalStorageDirectory()+"/"+MyCV.CVname+".pdf";

        //Fonts styles
        headingsFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
        normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        listFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            //float pageWidth = document.getPageSize().getWidth();

            //Header table with 2 column
            PdfPTable table = new PdfPTable(new float[]{1, 3});
            table.setWidthPercentage(100);

            //cell 1 of header table
            PdfPCell cell1 = new PdfPCell();
            cell1.setBorder(Rectangle.NO_BORDER);
            //add image
            if(personCV.getPerson().getImage() != null) {
                Bitmap imageBitmap = personCV.getPerson().getImage();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageByteArray = stream.toByteArray();
                Image image = Image.getInstance(imageByteArray);

                cell1.addElement(image);

                //image.scalePercent(30, 30);
                //image.setAlignment(Element.ALIGN_RIGHT);
                //Rectangle rectangle = document.getPageSize();
            }

            //2nd column of header table
            PdfPCell cell2 = new PdfPCell();
            //cell2.setBackgroundColor(BaseColor.GRAY);
            cell2.setBorder(Rectangle.NO_BORDER);
            cell2.setPaddingLeft(30f);
            Paragraph paragraph = new Paragraph(personCV.getPerson().getName(), headingsFont);
            cell2.addElement(paragraph);
            paragraph = new Paragraph(personCV.getPerson().getFather_name(), normalFont);
            cell2.addElement(paragraph);
            paragraph = new Paragraph(personCV.getPerson().getGender(), normalFont);
            cell2.addElement(paragraph);
            ContactDetails contactDetails = personCV.getPerson().getContactDetails();
            paragraph = new Paragraph(contactDetails.getCellNumber(), normalFont);
            cell2.addElement(paragraph);
            paragraph = new Paragraph(contactDetails.getAddress()+"\n"+contactDetails.getCity()+", "+contactDetails.getProvince()+contactDetails.getCountry(), normalFont);
            cell2.addElement(paragraph);
            paragraph = new Paragraph(contactDetails.getEmail(), normalFont);
            cell2.addElement(paragraph);

            table.addCell(cell1);
            table.addCell(cell2);
            document.add(table);
            //header end

            document.add(writeObjectiveToPdf());
            document.add(writeQualificationToPdf());
            document.add(writeExperienceToPdf());
            document.add(writeProjectsToPdf());
            document.add(writeObjectToPdf(personCV.getPerson().getSkills(), "Skills"));
            document.add(writeObjectToPdf(personCV.getPerson().getInterests(), "Interest"));
            document.add(writeObjectToPdf(personCV.getPerson().getHobbies(), "Hobbies"));
            document.add(writeProfileToPdf());
            document.add(writeReferenceToPdf());

            document.close();
        }catch (Exception e){
            Toast.makeText(this, "error in preview" +e.toString(), Toast.LENGTH_LONG).show();
        }

        //openGeneratedPDF();
    }
    private void openGeneratedPDF(){
        //File file = new File("/sdcard/pdffromlayout.pdf");
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+MyCV.CVname+".pdf");
        if (file.exists())
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //Uri uri = Uri.fromFile(file);
            Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName()+".provider", file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try
            {
                startActivity(intent);
            }
            catch(Exception e)
            {
                Toast.makeText(this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }else{
            showToast("Save First");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 10:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //permission was granted from popup
                    //previewCV();
                    saveCV();
                }
                else
                    {
                    Toast.makeText(this, "Access was denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    private void tableSetting(PdfPTable table){
        table.setWidthPercentage(100);
        table.setSpacingBefore(20f);
    }
    private void cellSetting(PdfPCell titleCell, PdfPCell descriptionCell, String title){
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setPaddingLeft(10f);
        titleCell.setPaddingBottom(20f);
        titleCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        Paragraph titleParagraph = new Paragraph(title, headingsFont);
        titleCell.addElement(titleParagraph);

        descriptionCell.setBorder(Rectangle.NO_BORDER);
        descriptionCell.setPaddingLeft(55f);
        descriptionCell.setPaddingRight(20f);
    }
    private PdfPTable writeObjectiveToPdf(){
        PdfPTable table = new PdfPTable(1);
        tableSetting(table);

        PdfPCell titleCell = new PdfPCell();
        PdfPCell descriptionCell = new PdfPCell();
        cellSetting(titleCell, descriptionCell, "Objective");

        //personCV.getPerson().setObjective("sample obbjective from code. We can set table cell's content alignment horizontally and vertically. To set the horizontal alignment we use the setHorizontalAlignment() method. To align it vertically we use the setVerticalAlignment() method. The alignment constant is defined in the com.itextpdf.text.Element class.");
        Paragraph objDescription = new Paragraph(personCV.getPerson().getObjective(), normalFont);
        descriptionCell.addElement(objDescription);

        table.addCell(titleCell);
        table.addCell(descriptionCell);

        return table;
    }
    private PdfPTable writeQualificationToPdf(){
        PdfPTable table = new PdfPTable(1);
        tableSetting(table);

        PdfPCell titleCell = new PdfPCell();
        PdfPCell descriptionCell = new PdfPCell();
        cellSetting(titleCell, descriptionCell, "Qualification");

       // Qualification q = new Qualification("MS Computer Science", "Virtual University of Pakistan", "4.0", "2020");
        //personCV.getPerson().getQualificationsList().add(q);

        for(int i = 0; i<personCV.getPerson().getQualificationsList().size(); i++){
            Qualification qualification = personCV.getPerson().getQualificationsList().get(i);

            Phrase phrase = new Phrase(qualification.getCourse(), listFont);
            List list = new List(List.UNORDERED);
            list.add(new ListItem(phrase));
            Paragraph courseTitle = new Paragraph();
            courseTitle.add(list);

            Paragraph courseDetails = new Paragraph();
            courseDetails.setIndentationLeft(20f);
            courseDetails.add(new Phrase(qualification.getInstitute()+"\n", normalFont));
            courseDetails.add(new Phrase(qualification.getGrade()+"\n", normalFont));
            courseDetails.add(new Phrase(qualification.getYear(), normalFont));

            descriptionCell.addElement(courseTitle);
            descriptionCell.addElement(courseDetails);
        }

        table.addCell(titleCell);
        table.addCell(descriptionCell);
        return table;
    }
    private PdfPTable writeExperienceToPdf(){
        PdfPTable table = new PdfPTable(1);
        tableSetting(table);

        PdfPCell titleCell = new PdfPCell();
        PdfPCell descriptionCell = new PdfPCell();
        cellSetting(titleCell, descriptionCell, "Experience");

        //Experience e = new Experience("Tiny Toons", "Junior Game Developer", "June 2017", "Sep 2017", "My Role was to hadle game functionality");
        //personCV.getPerson().getExperiencesList().add(e);

        for(int i = 0; i<personCV.getPerson().getExperiencesList().size(); i++){
            Experience experience = personCV.getPerson().getExperiencesList().get(i);

            Phrase phrase = new Phrase(experience.getOrgnization(), listFont);
            List list = new List(List.UNORDERED);
            list.add(new ListItem(phrase));
            Paragraph disgantion = new Paragraph();
            disgantion.add(list);

            Paragraph details = new Paragraph();
            details.setIndentationLeft(20f);
            details.add(new Phrase(experience.getDesignation()+"\n", normalFont));
            details.add(new Phrase(experience.getDateFrom()+" - "+experience.getDateTo()+"\n", normalFont));
            details.add(new Phrase(experience.getRole(), normalFont));

            descriptionCell.addElement(disgantion);
            descriptionCell.addElement(details);
        }

        table.addCell(titleCell);
        table.addCell(descriptionCell);
        return table;
    }
    private PdfPTable writeProjectsToPdf(){
        PdfPTable table = new PdfPTable(1);
        tableSetting(table);

        PdfPCell titleCell = new PdfPCell();
        PdfPCell descriptionCell = new PdfPCell();
        cellSetting(titleCell, descriptionCell, "Projects");

        //Projects p = new Projects("Online CV Maker", "it was my final year project in university.", "1 year");
        //personCV.getPerson().getProjectsList().add(p);

        for(int i = 0; i<personCV.getPerson().getProjectsList().size(); i++){
            Projects projects = personCV.getPerson().getProjectsList().get(i);

            Phrase phrase = new Phrase(projects.getTitle(), listFont);
            List list = new List(List.UNORDERED);
            list.add(new ListItem(phrase));
            Paragraph pTitle = new Paragraph();
            pTitle.add(list);

            Paragraph details = new Paragraph();
            details.setIndentationLeft(20f);
            details.add(new Phrase("Duration: "+ projects.getDuration() +"\n", normalFont));
            details.add(new Phrase(projects.getDescription(), normalFont));

            descriptionCell.addElement(pTitle);
            descriptionCell.addElement(details);
        }

        table.addCell(titleCell);
        table.addCell(descriptionCell);
        return table;
    }
    private PdfPTable writeObjectToPdf(String obj, String title){
        PdfPTable table = new PdfPTable(1);
        tableSetting(table);

        PdfPCell titleCell = new PdfPCell();
        PdfPCell descriptionCell = new PdfPCell();
        cellSetting(titleCell, descriptionCell, title);

        Paragraph objDescription = new Paragraph(obj, normalFont);
        descriptionCell.addElement(objDescription);

        table.addCell(titleCell);
        table.addCell(descriptionCell);

        return table;
    }
    private PdfPTable writeProfileToPdf(){
        PdfPTable table = new PdfPTable(1);
        tableSetting(table);

        PdfPCell titleCell = new PdfPCell();
        PdfPCell descriptionCell = new PdfPCell();
        cellSetting(titleCell, descriptionCell, "Personal Profile");

        PdfPTable objDescriptionTable = new PdfPTable(new float[]{1.5f, 2.5f});
        tableSetting(objDescriptionTable);
        PdfPCell cell1 = new PdfPCell();
        PdfPCell cell2 = new PdfPCell();
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);

        cell1.addElement(new Paragraph(" Date of Birth \n\n Father Name \n\n Marital Status \n\n Nationality \n\n Languages", normalFont));
        cell2.addElement(new Paragraph(": " + personCV.getPerson().getDob() + "\n\n: "+ personCV.getPerson().getFather_name() + "\n\n: "+ personCV.getPerson().getMarital_status() + "\n\n: "+ personCV.getPerson().getNationality() + "\n\n: "+ personCV.getPerson().getLanguages(), normalFont));

        objDescriptionTable.addCell(cell1);
        objDescriptionTable.addCell(cell2);
        descriptionCell.addElement(objDescriptionTable);

        table.addCell(titleCell);
        table.addCell(descriptionCell);

        return table;
    }
    private PdfPTable writeReferenceToPdf(){
        PdfPTable table = new PdfPTable(1);
        tableSetting(table);

        PdfPCell titleCell = new PdfPCell();
        PdfPCell descriptionCell = new PdfPCell();
        cellSetting(titleCell, descriptionCell, "Reference");

        PdfPTable objDescriptionTable = new PdfPTable(new float[]{1.5f, 2.5f});
        tableSetting(objDescriptionTable);
        PdfPCell cell1 = new PdfPCell();
        PdfPCell cell2 = new PdfPCell();
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);

        cell1.addElement(new Paragraph(" Name \n\n Designation \n\n Orgnization \n\n Email \n\n Phone", normalFont));
        Reference reference = personCV.getPerson().getReference();
        cell2.addElement(new Paragraph(": " + reference.getRefName() + "\n\n: "+ reference.getRefDesignation() + "\n\n: "+ reference.getRefOrgnization() + "\n\n: "+ reference.getRefMail() + "\n\n: "+ reference.getRefPhone(), normalFont));

        objDescriptionTable.addCell(cell1);
        objDescriptionTable.addCell(cell2);
        descriptionCell.addElement(objDescriptionTable);

        table.addCell(titleCell);
        table.addCell(descriptionCell);

        return table;
    }

    private void exportPupupWindow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Export");

        // Set up the input
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 5, 0, 0);
        final EditText input = new EditText(this);
        final CheckBox mailCheckBox = new CheckBox(this);
        final CheckBox fbCheckBox = new CheckBox(this);

        input.setHint("Enter mail here...");
        input.setVisibility(View.INVISIBLE);
        mailCheckBox.setText("Email");
        fbCheckBox.setText("Facebook");

        linearLayout.addView(input);
        linearLayout.addView(mailCheckBox);
        linearLayout.addView(fbCheckBox);

        mailCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    input.setVisibility(View.VISIBLE);
                }else
                    input.setVisibility(View.INVISIBLE);
            }
        });

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(linearLayout);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mailCheckBox.isChecked() && input.getText().toString() != null) {
                    email = input.getText().toString();
                    sendEmail();
                }else if(fbCheckBox.isChecked()){
                    shareToFB();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void sendEmail(){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+MyCV.CVname+".pdf");
        if (file.exists()) {
            try
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                //Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName()+".provider", file);
                //intent.setDataAndType(uri, "application/pdf");
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email}); //recipient email
                intent.putExtra(Intent.EXTRA_SUBJECT, "My Professional CV...");
                intent.putExtra(Intent.EXTRA_TEXT, "Here is my cv");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                startActivity(Intent.createChooser(intent, "choose mail app"));
            } catch(Exception e)
            {
                Toast.makeText(this, "send mail error : "+e.toString(), Toast.LENGTH_LONG).show();
                Log.d("mail_error", e.toString());
            }
        }else{
            showToast("Save or preview First");
        }
    }
    private void shareToFB(){
        CallbackManager callbackManager = CallbackManager.Factory.create();
        ShareDialog shareDialog = new ShareDialog(this);
        String pathName = Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+MyCV.CVname+".pdf";
        File file = new File(pathName);
        if(file.exists()) {
            try {
                ArrayList<SharePhoto> photoList = new ArrayList<>();
                int totalPages = new PdfReader(pathName).getNumberOfPages();

                for (int i = 0; i < totalPages; i++){
                    Bitmap imageBitmap = getBitmapFromPDF(file, i);
                    SharePhoto photo = new SharePhoto.Builder().setBitmap(imageBitmap).build();
                    //SharePhotoContent photoContent = new SharePhotoContent.Builder().addPhoto(photo).build();
                    photoList.add(photo);
                }
                SharePhotoContent photoContent = new SharePhotoContent.Builder().addPhotos(photoList).build();
                if(shareDialog.canShow(SharePhotoContent.class)){
                    shareDialog.show(photoContent);
                }

            } catch (Exception e) {
                Toast.makeText(this, "send mail error : " + e.toString(), Toast.LENGTH_LONG).show();
                Log.d("fb_error", e.toString());
            }
        }else{
            showToast("file not found save or preview first");
        }

    }
    public Bitmap getBitmapFromPDF(File file, int pageNumber){
        PdfiumCore pdfiumCore = new PdfiumCore(this);
        try {
            PdfDocument pdfDocument = pdfiumCore.newDocument(openFile(file));
            pdfiumCore.openPage(pdfDocument, pageNumber);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);


            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(width , height ,
                    Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNumber, 0, 0,
                    width, height);
            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param

            pdfiumCore.closeDocument(pdfDocument); // important!
            return bitmap;
        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(this, "getbitmap error" + ex.toString(), Toast.LENGTH_LONG).show();
            Log.d("Bitmap", ex.toString());
        }
        return null;
    }
    public static ParcelFileDescriptor openFile(File file) {
        ParcelFileDescriptor descriptor;
        try {
            descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(CVCreatingActivity.this, "filedescriptor  error" + e.toString(), Toast.LENGTH_LONG).show();
            Log.d("fild", e.toString());
            return null;
        }
        return descriptor;
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

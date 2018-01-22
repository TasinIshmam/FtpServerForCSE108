package ftp.modal;

import java.util.HashMap;
import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ClientAuthentification {
    public String userName;
    public String passWord;
    public static String inputFile = "BinaryContent/users.xls";
    public static final HashMap<String, ClientAuthentification> userHashMap = new HashMap();


    public ClientAuthentification(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public static void read() throws IOException {
        File inputWorkbook = new File(inputFile);
        Workbook w;

        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            //loop over first 10 column and lines


            for (int i = 0; i < sheet.getRows(); i++) {
                Cell userNameCell = sheet.getCell(0, i);
                Cell passWordCell = sheet.getCell(1, i);

                ClientAuthentification newClientUserName = new ClientAuthentification(userNameCell.getContents().toString(), passWordCell.getContents().toString());

                String newUserName = userNameCell.getContents().toString();

                userHashMap.put(newUserName, newClientUserName);
            }


        } catch (BiffException e) {
            e.printStackTrace();
        }


    }

}

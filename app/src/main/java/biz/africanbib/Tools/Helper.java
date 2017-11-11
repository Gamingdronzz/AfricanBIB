package biz.africanbib.Tools;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import biz.africanbib.Activity.MainActivity;
import biz.africanbib.Models.Add;
import biz.africanbib.Models.AddBuilder;
import biz.africanbib.Models.DropDown;
import biz.africanbib.Models.DropDownBuilder;
import biz.africanbib.Models.MultiSelectDropdown;
import biz.africanbib.Models.MultiSelectDropdownBuilder;
import biz.africanbib.Models.SimpleDate;
import biz.africanbib.Models.SimpleDateBuilder;
import biz.africanbib.Models.SimpleEditText;
import biz.africanbib.Models.SimpleEditTextBuilder;
import biz.africanbib.Models.SimpleImage;
import biz.africanbib.Models.SimpleImageBuilder;

/**
 * Created by Balpreet on 01-Aug-17.
 */

public class Helper {
    private Context context;
    public String FAILED = "failed";
    public String SUCCESS = "success";
    final String TAG = "Helper";
    private String baseURL = "http://www.imergesoft.de/android/";
    //private String baseURL = "http://www.gamingdronzz.com/gunjita/android/";


    public Helper(Context context) {
        this.context = context;
    }

    public Helper() {

    }


    public String getBaseURL() {
        return this.baseURL;
    }

    public JSONObject getJson(String input) {
        try {
            try {
                return new JSONObject(input.substring(input.indexOf("{"), input.indexOf("}") + 1));
            } catch (JSONException jse) {
                jse.printStackTrace();
                Log.v("Helper", "Error creating json");
                return null;
            }
        } catch (StringIndexOutOfBoundsException sioobe) {
            sioobe.printStackTrace();
            return null;
        }
    }

    public String getStringFromList(List list) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i).toString().toLowerCase());
            result.append(",");
        }
        result.deleteCharAt(result.length() - 1);
        Log.v("Helper", "Final String : " + result.toString());
        return result.toString();
    }

    public List<String> getListFromString(String string) {
        String[] intermediate = string.split(",");
        List<String> result = new ArrayList();
        for (String toLowerCase : intermediate) {
            result.add(toLowerCase.toLowerCase());
        }
        Log.v("Helper", "List Result : " + result);
        return result;
    }

    public Intent getImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        return intent;
    }

    public byte[] getByteArrayFromBitmap(Bitmap image) {
        if (image == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public Bitmap getBitmapFromString(String value) {
        byte[] inter = Base64.decode(value, 0);
        Log.d("Helper", "Byte Array = " + inter.toString());
        return BitmapFactory.decodeByteArray(inter, 0, inter.length);
    }

    public Bitmap getBitmapFromByteArray(byte[] value) {
        if (value != null) {
            return BitmapFactory.decodeByteArray(value, 0, value.length);
        }
        return null;
    }

    public Bitmap getBitmapFromResource(int res) {
        return BitmapFactory.decodeResource(this.context.getResources(), res);
    }

    public Map<String, String> getNameValuePairs(String value) {
        String[] result = value.split("\"");
        for (String s : result) {
            Log.d("Helper", "Split : " + s);
        }
        Map<String, String> finalResult = new HashMap();
        for (int i = 1; i < result.length - 2; i += 3) {
            finalResult.put(result[i], result[3]);
        }
        return finalResult;
    }

    public String checkForInput(String text) {
        if (text == null || text.length() == 0 || text.equals("")) {
            return null;
        }
        text = text.replaceAll("\\s+", "");
        if (text.length() == 0 || text.equals("")) {
            return null;
        }
        return text;
    }


    public String[] getCountryNames() {
        String[] names = new String[]{
                "Afghanistan",
                "Albania",
                "Algeria",
                "Andorra",
                "Angola",
                "Antigua and Barbuda",
                "Argentina",
                "Armenia",
                "Australia",
                "Austria",
                "Azerbaijan",
                "Bahamas",
                "Bahrain",
                "Bangladesh",
                "Barbados",
                "Belarus",
                "Belgium",
                "Belize",
                "Benin",
                "Bhutan",
                "Bolivia",
                "Bosnia and Herzegovina",
                "Botswana",
                "Brazil",
                "Brunei",
                "Bulgaria",
                "Burkina Faso",
                "Burundi",
                "Cabo Verde",
                "Cambodia",
                "Cameroon",
                "Canada",
                "Central African Republic (CAR)",
                "Chad",
                "Chile",
                "China",
                "Colombia",
                "Comoros",
                "Democratic Republic of the Congo",
                "Republic of the Congo",
                "Costa Rica",
                "Cote d'Ivoire",
                "Croatia",
                "Cuba",
                "Cyprus",
                "Czech Republic",
                "Denmark",
                "Djibouti",
                "Dominica",
                "Dominican Republic",
                "Ecuador",
                "Egypt",
                "El Salvador",
                "Equatorial Guinea",
                "Eritrea",
                "Estonia",
                "Ethiopia",
                "Fiji",
                "Finland",
                "France",
                "Gabon",
                "Gambia",
                "Georgia",
                "Germany",
                "Ghana",
                "Greece",
                "Grenada",
                "Guatemala",
                "Guinea",
                "Guinea-Bissau",
                "Guyana",
                "Haiti",
                "Honduras",
                "Hungary",
                "Iceland",
                "India",
                "Indonesia",
                "Iran",
                "Iraq",
                "Ireland",
                "Israel",
                "Italy",
                "Jamaica",
                "Japan",
                "Jordan",
                "Kazakhstan",
                "Kenya",
                "Kiribati",
                "Kosovo",
                "Kuwait",
                "Kyrgyzstan",
                "Laos",
                "Latvia",
                "Lebanon",
                "Lesotho",
                "Liberia",
                "Libya",
                "Liechtenstein",
                "Lithuania",
                "Luxembourg",
                "Macedonia (FYROM)",
                "Madagascar",
                "Malawi",
                "Malaysia",
                "Maldives",
                "Mali",
                "Malta",
                "Marshall Islands",
                "Mauritania",
                "Mauritius",
                "Mexico",
                "Micronesia",
                "Moldova",
                "Monaco",
                "Mongolia",
                "Montenegro",
                "Morocco",
                "Mozambique",
                "Myanmar (Burma)",
                "Namibia",
                "Nauru",
                "Nepal",
                "Netherlands",
                "New Zealand",
                "Nicaragua",
                "Niger",
                "Nigeria",
                "North Korea",
                "Norway",
                "Oman",
                "Pakistan",
                "Palau",
                "Palestine",
                "Panama",
                "Papua New Guinea",
                "Paraguay",
                "Peru",
                "Philippines",
                "Poland",
                "Portugal",
                "Qatar",
                "Romania",
                "Russia",
                "Rwanda",
                "Saint Kitts and Nevis",
                "Saint Lucia",
                "Saint Vincent and the Grenadines",
                "Samoa",
                "San Marino",
                "Sao Tome and Principe",
                "Saudi Arabia",
                "Senegal",
                "Serbia",
                "Seychelles",
                "Sierra Leone",
                "Singapore",
                "Slovakia",
                "Slovenia",
                "Solomon Islands",
                "Somalia",
                "South Africa",
                "South Korea",
                "South Sudan",
                "Spain",
                "Sri Lanka",
                "Sudan",
                "Suriname",
                "Swaziland",
                "Sweden",
                "Switzerland",
                "Syria",
                "Taiwan",
                "Tajikistan",
                "Tanzania",
                "Thailand",
                "Timor-Leste",
                "Togo",
                "Tonga",
                "Trinidad and Tobago",
                "Tunisia",
                "Turkey",
                "Turkmenistan",
                "Tuvalu",
                "Uganda",
                "Ukraine",
                "United Arab Emirates (UAE)",
                "United Kingdom (UK)",
                "United States of America (USA)",
                "Uruguay",
                "Uzbekistan",
                "Vanuatu",
                "Vatican City (Holy See)",
                "Venezuela",
                "Vietnam",
                "Yemen",
                "Zambia",
                "Zimbabwe"
        };
        return names;

    }

    public boolean isTab() {
        boolean isTab = (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        Log.d("Company", "Tab = " + isTab);
        return isTab;
    }

    public SimpleEditText buildEditText(String title, String value, String tableName, String columnName, int rowno, String xmlTag) {

        if (MainActivity.typeOfBusiness == MainActivity.NEWBUSINESS) {
            return new SimpleEditTextBuilder()
                    .setTitle(title)
                    .setTableName(tableName)
                    .setColumnName(columnName)
                    .setRowno(rowno)
                    .setXmlTag(xmlTag)
                    .createSimpleEditText();
        } else {
            return new SimpleEditTextBuilder()
                    .setTitle(title)
                    .setValue(value)
                    .setTableName(tableName)
                    .setColumnName(columnName)
                    .setRowno(rowno)
                    .setXmlTag(xmlTag)
                    .createSimpleEditText();
        }

    }

    public DropDown buildDropDown(String heading, String[] list, int selectedPosition, String tableName, String columnName, int rowno, String xmlTag) {
        if (MainActivity.typeOfBusiness == MainActivity.NEWBUSINESS) {

            return new DropDownBuilder()
                    .setHeading(heading)
                    .setList(list)
                    .setTableName(tableName)
                    .setColumnName(columnName)
                    .setRowno(rowno)
                    .setXmlTag(xmlTag)
                    .createDropDown();
        } else {
            return new DropDownBuilder()
                    .setHeading(heading)
                    .setList(list)
                    .setSelectedPosition(selectedPosition)
                    .setTableName(tableName)
                    .setXmlTag(xmlTag)
                    .setColumnName(columnName)
                    .setRowno(rowno)
                    .createDropDown();
        }
    }

    public Add buildAdd(int rows, String[] titles, String tableName, String[] tableColumnNames, String[] xmlTags) {
        return new AddBuilder()
                .setRows(rows)
                .setTableName(tableName)
                .setColumnNames(titles)
                .setxmlTags(xmlTags)
                .setTableColumnNames(tableColumnNames)
                .createAdd();
    }

    public MultiSelectDropdown buildMultiSelectDropdown(String title, String tableName, String columnName, String[] items, int[] uids, List<Integer> selectedIndices, int rowno, String xmlTag) {
        //Log.v("Helper","Selected Indices for " + title + " = " + selectedIndices.toString());
        return new MultiSelectDropdownBuilder()
                .setRowno(rowno)
                .setTitle(title)
                .setTableName(tableName)
                .setColumnName(columnName)
                .setItems(items)
                .setItemUid(uids)
                .setSelectedIndices(selectedIndices)
                .setXmlTag(xmlTag)
                .createMultiSelectDropdown();
    }

    public SimpleDate buildDate(String title, String value, String tableName, String columnName, int rowno, String xmlTag) {

        if (MainActivity.typeOfBusiness == MainActivity.NEWBUSINESS) {
            return new SimpleDateBuilder()
                    .setTitle(title)
                    .setTableName(tableName)
                    .setColumnName(columnName)
                    .setRowno(rowno)
                    .setXmlTag(xmlTag)
                    .createSimpleDate();
        } else {
            return new SimpleDateBuilder()
                    .setTitle(title)
                    .setValue(value)
                    .setTableName(tableName)
                    .setColumnName(columnName)
                    .setRowno(rowno)
                    .setXmlTag(xmlTag)
                    .createSimpleDate();
        }

    }

    public SimpleImage buildImage(String title, int rowNo, Bitmap image, String tableName, String columnName, String xmlTag) {

        if (MainActivity.typeOfBusiness == MainActivity.NEWBUSINESS) {
            return new SimpleImageBuilder()
                    .setTitle(title)
                    .setRowNo(rowNo)
                    .setTableName(tableName)
                    .setColumnName(columnName)
                    .setXmlTag(xmlTag)
                    .createSimpleImage();
        } else {
            return new SimpleImageBuilder()
                    .setTitle(title)
                    .setRowNo(rowNo)
                    .setTableName(tableName)
                    .setColumnName(columnName)
                    .setImage(image)
                    .setXmlTag(xmlTag)
                    .createSimpleImage();
        }

    }

    public List<Integer> getSelectedIndices(String list) {
        if (list == null)
            return null;
        List<Integer> result = new ArrayList<>();
        String[] values = list.split("a");
        for (String s :
                values) {
            result.add(Integer.parseInt(s));
        }
        Log.v("Helper", "Creating list from " + list);
        return result;
    }

    public String getStringFromIndices(List<Integer> list) {
        StringBuilder builder = new StringBuilder();
        for (int i :
                list) {
            builder.append(i + "a");
        }
        Log.v("Helper", "Creating string from " + list.toString());
        return builder.toString();
    }


    public String[] getIndustryList() {
        return new String[]
                {
                        "Agriculture/Livestock",
                        "Banking, Finance & Insurance",
                        "Architecture & Engineering",
                        "Art / Media / Design",
                        "Business Services",
                        "Chemicals & Pharmaceuticals",
                        "Food & Related Products",
                        "Energy & Raw Materials",
                        "Metalworking & Metallurgy",
                        "Industry & Equipment (Machines & Equipment)",
                        "Vehicles & Transport Equipment",
                        "Rubber & Plastics",
                        "Woodwork, Furniture & Crafts",
                        "Glass & Construction Materials",
                        "Precision Equipment",
                        "Information Technology",
                        "Telecommunication Industry",
                        "Electrical & Electronic Equipment",
                        "Paper & Cardboard",
                        "Leather & Shoes",
                        "Textiles & Clothing",
                        "Printing & Publishing",
                        "Luxury & Leisure Products",
                        "Transport & Related Services",
                        "Travel, Tourism & Leisure",
                        "Marketing, Advertising & the Media",
                        "Health",
                        "Education and Trainings",
                        "General Education Sector"
                };
    }


    public String[] manageMultiSelectList(int i) {
        String[] result = null;
        switch (i) {
            case 0:
                result = new String[]{
                        "Horticulture & Forestry",
                        "Fruits & Vegetables",
                        "Plants & Fertilizers",
                        "Cereals & Grains",
                        "Animal Production",
                        "Agricultural Production",
                        "Animals & Livestock"
                };
                break;
            case 1:
                result = new String[]{
                        "Banking Institutions",
                        "Tax Services",
                        "Insurance",
                        "Financial Consultants",
                        "Venture Capital & Private Equity",
                        "Micro & SME Finance",
                        "Payment Systems, securities clearance & settlement",
                        "Non-compulsory pensions"
                };
                break;
            case 2:
                result = new String[]{
                        "Aerospace",
                        "Civil Engineering",
                        "Finishings",
                        "Electrical Engineering",
                        "Architecture & Planning",
                        "Construction",
                        "Equipment",
                        "Structural Engineering",
                        "Traffic Engineering",
                        "Mechanical/Industial Engineering",
                        "Public Works",
                        "Landscape Architecture",
                        "Building Materials",
                        "Metrology/Control Engineering"
                };
                break;
            case 3:
                result = new String[]{
                        "Design Online Media",
                        "Photography",
                        "Media Production",
                        "Print Media",
                        "Graphic Design",
                        "Design",
                        "Online Media"
                };
                break;
            case 4:
                result = new String[]{
                        "Administrative Services",
                        "Design & Research Offices",
                        "Packaging",
                        "Retail Outlets",
                        "Business Consultancy",
                        "Environment",
                        "Property",
                        "Training",
                        "Cleaning",
                        "Maintenance & Repairs",
                        "Professional Bodies",
                        "Translating & Interpreting",
                        "Storage",
                        "Security-Services"
                };
                break;
            case 5:
                result = new String[]{
                        "Cosmetics",
                        "Pharmaceuticals",
                        "Chemicals - Basic Products & Derivatives"
                };
                break;
            case 6:
                result = new String[]{
                        "Bread & Cakes",
                        "Frozen Foods",
                        "Dairy Products",
                        "Meats",
                        "Drinks",
                        "Tinned Foods",
                        "Food Processing",
                        "Wines"
                };
                break;
            case 7:
                result = new String[]{
                        "Energy",
                        "Raw Materials",
                        "Stones & Minerals",
                        "Water"
                };
                break;
            case 8:
                result = new String[]{
                        "Sheet Metal & Tubes",
                        "Steel & Metals",
                        "Finished Metal Products",
                        "Tools & Hardware",
                        "Steel & Metal Transformation"
                };
                break;
            case 9:
                result = new String[]{
                        "Agriculture",
                        "Construction",
                        "Environment",
                        "Wood",
                        "Handling",
                        "Metals",
                        "Plastics & Rubber",
                        "Temperature Control",
                        "Chemicals & Pharmaceutics",
                        "Food Industry",
                        "Maritime",
                        "Paper / Printing",
                        "Textiles",
                };
                break;
            case 10:
                result = new String[]{
                        "Aeronautics",
                        "Industrial Vehicles",
                        "Boats",
                        "Material & Equipment",
                        "Cars",
                        "Cycles",
                        "Rental"
                };
                break;
            case 11:
                result = new String[]{
                        "Packaging -Plastic",
                        "Plastics",
                        "Rubber"
                };
                break;
            case 12:
                result = new String[]{
                        "Furniture for Business",
                        "Home Furniture",
                        "Wood",
                        "Wood Carving",
                        "Wood - Finished Products"
                };
                break;
            case 13:
                result = new String[]{
                        "Ceramics",
                        "Construction Materials",
                        "Glass"
                };
                break;
            case 14:
                result = new String[]{
                        "Clock / Watch Making",
                        "Measurements - Equipment / Instruments",
                        "Optical Equipment / Instruments"
                };
                break;
            case 15:
                result = new String[]{
                        "PC Hardware",
                        "IT Services",
                        "Peripherals - PC",
                        "Software"
                };
                break;
            case 16:
                result = new String[]{
                        "Audio - Visual",
                        "Cables & Networks",
                        "Internet & Telephony",
                        "Office Automation"
                };
                break;
            case 17:
                result = new String[]{
                        "Electrical & Electronic Components",
                        "Hi-Fi & Household Appliances",
                        "Electronic Equipment",
                        "Motors",
                        "Electricity",
                        "Security",
                        "Lighting"
                };
                break;
            case 18:
                result = new String[]{
                        "Finished Products",
                        "Paper raw Materials",
                        "Packaging Materials – Paper & Cardboard"
                };
                break;
            case 19:
                result = new String[]{
                        "Leather – Raw Materials",
                        "Leather Goods",
                        "Shoes"
                };
                break;
            case 20:
                result = new String[]{
                        "Clothes",
                        "Clothing Accessories",
                        "Fabrics",
                        "Household Linen & Fabrics"
                };
                break;
            case 21:
                result = new String[]{
                        "Electronic Publishing",
                        "Printing",
                        "Publishing",
                        "Electronic ",
                        "Publishing Printing"
                };
                break;
            case 22:
                result = new String[]{
                        "Art & Entertainment",
                        "Jewelry",
                        "Co-packing",
                        "Presents",
                        "Games & Toys",
                        "Sport - Articles & Equipment"
                };
                break;
            case 23:
                result = new String[]{
                        "Air Transport",
                        "Sea & River Transport",
                        "Rail Transport",
                        "Specialized Transport",
                        "Road Transport",
                        "Transport Auxiliaries"
                };
                break;
            case 24:
                result = new String[]{
                        "Hotel Business & Catering",
                        "Sports & Leisure Activities",
                        "Trade Shows",
                        "Travel"
                };
                break;
            case 25:
                result = new String[]{
                        "Advertising",
                        "Advertising Media",
                        "Marketing",
                        "Print Media"
                };
                break;
            case 26:
                result = new String[]{
                        "Biotechnologies ",
                        "Medical Services ",
                        "Rehabilitation",
                        "Fitness & Spa ",
                        "Medical Equipment ",
                        "Home Care",
                        "Health Centers ",
                        "Physical Therapy ",
                        "Nursing"
                };
                break;
            case 27:
                result = new String[]{
                        "Academia ",
                        "E-learning Centers ",
                        "Adult literacy/non-formal education",
                        "Libraries ",
                        "Daycare ",
                        "Pre-primary education"
                };
                break;
            case 28:
                result = new String[]{
                        "Primary education ",
                        "University ",
                        "Secondary education",
                        "Vocational training ",
                        "Tertiary education"
                };
                break;
        }
        return result;

    }


    public int[] manageMultiSelectList2(int i) {
        int[] result = null;
        switch (i) {
            case 0:
                result = new int[]{1, 2, 3, 4, 5, 6, 7};
                break;
            case 1:
                result = new int[]{8, 9, 10, 11, 12, 307, 308, 309};
                break;
            case 2:
                result = new int[]{13, 14, 15, 16, 17, 18, 310, 311, 312, 313, 314, 315, 316, 317};
                break;
            case 3:
                result = new int[]{19, 20, 21, 22, 135, 136, 137};
                break;
            case 4:
                result = new int[]{23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36};
                break;
            case 5:
                result = new int[]{37, 38, 39};
                break;
            case 6:
                result = new int[]{40, 41, 42, 43, 44, 45, 46, 47};
                break;
            case 7:
                result = new int[]{48, 49, 50, 51};
                break;
            case 8:
                result = new int[]{52, 53, 54, 55, 56};
                break;
            case 9:
                result = new int[]{57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69};
                break;
            case 10:
                result = new int[]{70, 71, 72, 73, 74, 75, 76};
                break;
            case 11:
                result = new int[]{77, 78, 79};
                break;
            case 12:
                result = new int[]{80, 81, 82, 83, 84};
                break;
            case 13:
                result = new int[]{85, 86, 87};
                break;
            case 14:
                result = new int[]{88, 89, 90};
                break;
            case 15:
                result = new int[]{91, 92, 93, 94};
                break;
            case 16:
                result = new int[]{95, 96, 97, 98};
                break;
            case 17:
                result = new int[]{99, 100, 101, 102, 103, 104, 105};
                break;
            case 18:
                result = new int[]{106, 107, 108};
                break;
            case 19:
                result = new int[]{109, 110, 111};
                break;
            case 20:
                result = new int[]{112, 113, 114, 115};
                break;
            case 21:
                result = new int[]{116, 117, 118, 235, 236};
                break;
            case 22:
                result = new int[]{119, 120, 121, 122, 123, 124};
                break;
            case 23:
                result = new int[]{125, 126, 127, 128, 129, 130};
                break;
            case 24:
                result = new int[]{131, 132, 133, 134};
                break;
            case 25:
                result = new int[]{254, 255, 256, 257};
                break;
            case 26:
                result = new int[]{258, 259, 260, 261, 262, 263, 264, 265, 266};
                break;
            case 27:
                result = new int[]{267,
                        268,
                        269,
                        270,
                        271,
                        272,
                };
                break;
            case 28:
                result = new int[]{273,
                        274,
                        275,
                        276,
                        277,
                };
                break;
        }
        return result;
    }

    public void childTags(MultiSelectDropdown multiSelectDropdown, int index, XmlSerializer xmlSerializer) {
        if (multiSelectDropdown.getColumnName().equals(DatabaseHelper.COLUMN_SECTOR)) {
            try {
                xmlSerializer.startTag(null, "uid");
                xmlSerializer.text(String.valueOf(getIdFromSelectedIndex(multiSelectDropdown.getItemUid(), index)));
                xmlSerializer.endTag(null, "uid");
                xmlSerializer.startTag(null, "name");
                xmlSerializer.text(getStringFromSelectedIndex(multiSelectDropdown.getItems(), index));
                xmlSerializer.endTag(null, "name");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (multiSelectDropdown.getColumnName().equals(DatabaseHelper.COLUMN_LANGUAGE)) {
            try {
                xmlSerializer.startTag(null, "uid");
                xmlSerializer.text(String.valueOf(getIdFromSelectedIndex(multiSelectDropdown.getItemUid(), index)));
                xmlSerializer.endTag(null, "uid");
                xmlSerializer.startTag(null, "language");
                xmlSerializer.text(getStringFromSelectedIndex(multiSelectDropdown.getItems(), index));
                xmlSerializer.endTag(null, "language");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] createByteArrayFromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        }
        return null;
    }

    public Bitmap createBitmapFromByteArray(byte[] array) {
        if (array != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(array, 0, array.length);
            return bmp;
        }
        return null;
    }

    public String getStringFromSelectedIndex(String[] items, int index) {
        return items[index];
    }

    public int getIdFromSelectedIndex(int[] ids, int index) {
        return ids[index];
    }

    public static String forReplacementString(String aInput) {
        return Matcher.quoteReplacement(aInput);
    }

    public String toDays(String date) {
        Date d = null;
        if (date != null) {
            try {
                d = new SimpleDateFormat("DD/MM/yyyy").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long days = TimeUnit.DAYS.convert(d.getTime(), TimeUnit.MILLISECONDS);
            return String.valueOf(days);

        } else return " ";
    }
}




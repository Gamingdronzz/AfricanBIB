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

import java.io.ByteArrayOutputStream;
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

    public MultiSelectDropdown buildMultiSelectDropdown(String title, String tableName, String columnName, String[] items, List<Integer> selectedIndices, int rowno, String xmlTag) {
        //Log.v("Helper","Selected Indices for " + title + " = " + selectedIndices.toString());
        return new MultiSelectDropdownBuilder()
                .setRowno(rowno)
                .setTitle(title)
                .setTableName(tableName)
                .setColumnName(columnName)
                .setItems(items)
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

    public SimpleImage buildImage(String title,  int rowNo ,Bitmap image, String tableName, String columnName, String xmlTag) {

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
                        "Cereals & Grains	",
                        "Fruits & Vegetables",
                        "Animal Production",
                        "Plants & Fertilizers",
                        "Agricultural Production",
                        "Animals & Livestock"
                };
                break;
            case 1:
                result = new String[]{
                        "Banking Institutions",
                        "Tax Services",
                        "Non-Compulsory Pensions",
                        "Insurance",
                        "Financial Consultants",
                        "insurance & Savings",
                        "Venture capital/Private Equity",
                        "Micro & SME Finance",
                        "Payment Systems, Securities Clearance & Settlement"
                };
                break;
            case 2:
                result = new String[]{
                        "Aerospace",
                        "Electrical Engineering",
                        "Equipment",
                        "Structural Engineering",
                        "Traffic Engineering",
                        "Civil Engineering",
                        "Architecture & Planning",
                        "Finishings",
                        "Mechanical / Industrial Engineering",
                        "Public Works",
                        "Landscape Architecture",
                        "Construction",
                        "Building Materials",
                        "Metrology / Control Engineering"
                };
                break;
            case 3:
                result = new String[]{
                        "Design",
                        "Media Production",
                        "Online Media",
                        "Print Media",
                        "Graphic Design",
                        "Photography"
                };
                break;
            case 4:
                result = new String[]{
                        "Administrative Services",
                        "Retail Outlets",
                        "Property",
                        "Translating & Interpreting",
                        "Storage",
                        "Design/Research Offices",
                        "Maintenance/Repairs",
                        "Training",
                        "Professional Bodies",
                        "Security Services",
                        "Packaging",
                        "Environment",
                        "Cleaning"
                };
                break;
            case 5:
                result = new String[]{
                        "Chemicals – Basic Products & Derivates",
                        "Pharmaceuticals",
                        "Cosmetics"
                };
                break;
            case 6:
                result = new String[]{
                        "Bread & Cakes",
                        "Meats",
                        "Food Processing",
                        "Frozen Foods",
                        "Drinks",
                        "Wines",
                        "Dairy Products",
                        "Tinned Foods"
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
                        "Sheet Material & Tubes",
                        "Tools & Hardware",
                        "Steels & Metals",
                        "Transformation",
                        "Steels & Metals",
                        "Finished Metal Products"
                };
                break;
            case 9:
                result = new String[]{
                        "Agriculture",
                        "Environment",
                        "Metals",
                        "Temperature Control",
                        "Food Industry",
                        "Paper / Printing",
                        "Wood",
                        "Textiles",
                        "Construction",
                        "Handling",
                        "Plastics & Rubber",
                        "Chemicals & Pharmaceutics",
                        "Maritime"
                };
                break;
            case 10:
                result = new String[]{
                        "Aeronautics",
                        "Materials & Equipment",
                        "Rental",
                        "Industrial Vehicles",
                        "Cars",
                        "Boats",
                        "Cycles"
                };
                break;
            case 11:
                result = new String[]{
                        "Packaging - Plastic",
                        "Plastics",
                        "Rubber"
                };
                break;
            case 12:
                result = new String[]{
                        "Furniture for Business",
                        "Wood Carving",
                        "Home Furniture",
                        "Wood – Finished Products",
                        "Wood"
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
                        "Motors",
                        "Lighting",
                        "Electronic Equipment",
                        "Electricity",
                        "Security",
                        "Hi-Fi & Household Appliances",
                        "Electrical / Electronic Components"
                };
                break;
            case 18:
                result = new String[]{
                        "Finished Products",
                        "Raw Materials",
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
                        "Publishing"
                };
                break;
            case 22:
                result = new String[]{
                        "Arts & Entertainment",
                        "Presents",
                        "Sport - Articles & Equipment",
                        "Jewellery",
                        "Games & Toys",
                        "Co-packing"
                };
                break;
            case 23:
                result = new String[]{
                        "Air Transport",
                        "Specialized Transport",
                        "Sea & River Transport",
                        "Road Transport",
                        "Rail Transport",
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
                        "Biotechnologies",
                        "Fitness & Spa",
                        "Nursing",
                        "Health Centres",
                        "Medical Services",
                        "Medical Equipment",
                        "Physical Therapy",
                        "Rehabilitation",
                        "Home Care"
                };
                break;
            case 27:
                result = new String[]{
                        "Academia",
                        "Libraries",
                        "E-learning Centers",
                        "Day care",
                        "Adult Literacy / Non-Formal Education",
                        "Pre - Primary Education"
                };
                break;
            case 28:
                result = new String[]{
                        "Primary Education",
                        "University",
                        "Secondary Education",
                        "Tertiary Education",
                        "Vocational Training"
                };
                break;
        }
        return result;
        //notifyDataSetChanged();
        //items.add(position+1,multiSelectDropdown);
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


    public static String forReplacementString(String aInput) {
        return Matcher.quoteReplacement(aInput);
    }

    public String toDays(String date) {
        Date d = null;
        if (date != null) {
            try {
                d = new SimpleDateFormat("DD/MM/YYYY").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long days = TimeUnit.DAYS.convert(d.getTime(), TimeUnit.MILLISECONDS);
            return String.valueOf(days);

        } else return " ";
    }
}




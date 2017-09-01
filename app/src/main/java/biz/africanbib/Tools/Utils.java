package biz.africanbib.Tools;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import biz.africanbib.MainActivity;
import biz.africanbib.Models.Add;
import biz.africanbib.Models.AddBuilder;
import biz.africanbib.Models.DropDown;
import biz.africanbib.Models.DropDownBuilder;
import biz.africanbib.Models.MultiSelectDropdown;
import biz.africanbib.Models.MultiSelectDropdownBuilder;
import biz.africanbib.Models.SimpleEditText;
import biz.africanbib.Models.SimpleEditTextBuilder;

/**
 * Created by Balpreet on 01-Aug-17.
 */

public class Utils {
    private Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public Utils() {

    }

    public String[] getCountryNames()
    {
        String [] names = new String[]{
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
        return  names;

    }

    public boolean isTab()
    {
        boolean isTab = (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        Log.d("Company","Tab = " + isTab);
        return isTab;
    }

    public SimpleEditText buildEditText(String title, String value, String tableName, String columnName,int rowno) {

        if(MainActivity.typeOfBusiness == MainActivity.NEWBUSINESS)
        {
            return new SimpleEditTextBuilder()
                    .setTitle(title)
                    .setTableName(tableName)
                    .setColumnName(columnName)
                    .setRowno(rowno)
                    .createSimpleEditText();
        }
        else
        {
            return new SimpleEditTextBuilder()
                    .setTitle(title)
                    .setValue(value)
                    .setTableName(tableName)
                    .setColumnName(columnName)
                    .setRowno(rowno)
                    .createSimpleEditText();
        }

    }

    public DropDown buildDropDown(String heading, String[] list, int selectedPosition, String tableName, String columnName,int rowno) {
        if(MainActivity.typeOfBusiness == MainActivity.NEWBUSINESS) {

            return new DropDownBuilder()
                    .setHeading(heading)
                    .setList(list)
                    .setTableName(tableName)
                    .setColumnName(columnName)
                    .setRowno(rowno)
                    .createDropDown();
        }
        else
        {
            return new DropDownBuilder()
                    .setHeading(heading)
                    .setList(list)
                    .setSelectedPosition(selectedPosition)
                    .setTableName(tableName)
                    .setColumnName(columnName)
                    .setRowno(rowno)
                    .createDropDown();
        }
    }

    public Add buildAdd(int rows,String[] titles,String tableName,String[] tableColumnNames)
    {
        return new AddBuilder()
                .setRows(rows)
                .setTableName(tableName)
                .setColumnNames(titles)
                .setTableColumnNames(tableColumnNames)
                .createAdd();
    }

    public MultiSelectDropdown buildMultiSelectDropdown( String title, String tableName, String columnName, String[] items,List<Integer> selectedIndices,int rowno)
    {
        //Log.v("Utils","Selected Indices for " + title + " = " + selectedIndices.toString());
        return new MultiSelectDropdownBuilder()
            .setRowno(rowno)
            .setTitle(title)
            .setTableName(tableName)
            .setColumnName(columnName)
            .setItems(items)
            .setSelectedIndices(selectedIndices)
            .createMultiSelectDropdown();
    }

    public List<Integer> getSelectedIndices(String list)
    {
        if(list == null)
            return null;
        List<Integer> result = new ArrayList<>();
        String[] values = list.split("a");
        for (String s :
                values) {
            result.add(Integer.parseInt(s));
        }
        Log.v("Utils","Creating list from " + list);
        return result;
    }

    public String getStringFromIndices(List<Integer> list)
    {
        StringBuilder builder = new StringBuilder();
        for (int i :
                list) {
            builder.append(i+"a");
        }
        Log.v("Utils","Creating string from " + list.toString());
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
        String[]  result = null;
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
}

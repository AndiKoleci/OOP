package input;

import simulation.AnnualChange;
import simulation.Child;
import simulation.ChildUpdate;
import simulation.Gift;
import simulation.InitialData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public final class Input {

    //Clasa Input este Singleton, facut cu Eager instantiation
    private static final Input INPUT = new Input();

    private int numberOfYears;
    private double santaBudget;
    private InitialData initialData;
    private ArrayList<AnnualChange> annualChanges;

    private Input() {
    }

    public static Input getInput() {
        return INPUT;
    }

    public int getNumberOfYears() {
        return numberOfYears;
    }

    public double getSantaBudget() {
        return santaBudget;
    }

    public InitialData getInitialData() {
        return initialData;
    }

    public ArrayList<AnnualChange> getAnnualChanges() {
        return annualChanges;
    }

    /**
     * Reads the input from each file and adds each value to the
     * specific field
     *
     * @param object - JSONObject resulted from parsing the file
     */
    public void readInput(final JSONObject object) {
        Long a = (Long) object.get("numberOfYears");
        this.numberOfYears = a.intValue();
        Long b = (Long) object.get("santaBudget");
        this.santaBudget = b.doubleValue();

        InitialData newInitialData = new InitialData();
        JSONObject initialDataObj = (JSONObject) object.get("initialData");
        JSONArray children = (JSONArray) initialDataObj.get("children");
        ArrayList<Child> childrenList = new ArrayList<>();
        for (Object value : children) {
            JSONObject child = (JSONObject) value;
            Long getvalue = (Long) child.get("id");
            int id = getvalue.intValue();
            String lastName = (String) child.get("lastName");
            String firstName = (String) child.get("firstName");
            getvalue = (Long) child.get("age");
            int age = getvalue.intValue();
            String city = (String) child.get("city");
            getvalue = (Long) child.get("niceScore");
            double niceScore = getvalue.doubleValue();
            ArrayList<String> newGiftString = new ArrayList<>();
            JSONArray giftsPreference = (JSONArray) child.get("giftsPreferences");
            for (Object string : giftsPreference) {
                String giftStringToAdd = (String) string;
                newGiftString.add(giftStringToAdd);
            }
            getvalue = (Long) child.get("niceScoreBonus");
            double niceScoreBonus = getvalue.doubleValue();
            String elf = (String) child.get("elf");
            Child newChild = new Child(id, lastName, firstName, age, city,
                    niceScore, newGiftString, niceScoreBonus, elf);
            childrenList.add(newChild);
        }
        newInitialData.setChildren(childrenList);

        JSONArray gifts = (JSONArray) initialDataObj.get("santaGiftsList");
        ArrayList<Gift> santaGifts = new ArrayList<>();
        for (Object o : gifts) {
            JSONObject gift = (JSONObject) o;
            String name = (String) gift.get("productName");
            Long getvalue = (Long) gift.get("price");
            double price = getvalue.doubleValue();
            String category = (String) gift.get("category");
            getvalue = (Long) gift.get("quantity");
            int quantity = getvalue.intValue();
            Gift newGift = new Gift(name, price, category, quantity);
            santaGifts.add(newGift);
        }
        newInitialData.setSantaGiftsList(santaGifts);
        this.initialData = newInitialData;

        JSONArray annualChangesObj = (JSONArray) object.get("annualChanges");
        ArrayList<AnnualChange> annualChanges1 = new ArrayList<>();
        for (Object o : annualChangesObj) {
            JSONObject change = (JSONObject) o;
            Long getvalue = (Long) change.get("newSantaBudget");
            double newSantaBudget = getvalue.doubleValue();
            JSONArray newGifts = (JSONArray) change.get("newGifts");
            ArrayList<Gift> giftsToAdd = new ArrayList<>();
            for (Object obj : newGifts) {
                JSONObject newGift = (JSONObject) obj;
                String name = (String) newGift.get("productName");
                Long newPrice = (Long) newGift.get("price");
                double price = newPrice.doubleValue();
                String category = (String) newGift.get("category");
                getvalue = (Long) newGift.get("quantity");
                int quantity = getvalue.intValue();
                Gift giftToAdd = new Gift(name, price, category, quantity);
                giftsToAdd.add(giftToAdd);
            }
            JSONArray newChildren = (JSONArray) change.get("newChildren");
            ArrayList<Child> childrenToAdd = new ArrayList<>();
            for (Object obj : newChildren) {
                JSONObject newChild = (JSONObject) obj;
                Long newID = (Long) newChild.get("id");
                int id = newID.intValue();
                String lastName = (String) newChild.get("lastName");
                String firstName = (String) newChild.get("firstName");
                getvalue = (Long) newChild.get("age");
                int age = getvalue.intValue();
                String city = (String) newChild.get("city");
                getvalue = (Long) newChild.get("niceScore");
                double niceScore = getvalue.doubleValue();
                ArrayList<String> newGiftString = new ArrayList<>();
                JSONArray giftsPreference = (JSONArray) newChild.get("giftsPreferences");
                for (Object string : giftsPreference) {
                    String giftStringToAdd = (String) string;
                    newGiftString.add(giftStringToAdd);
                }
                getvalue = (Long) newChild.get("niceScoreBonus");
                double niceScoreBonus = getvalue.doubleValue();
                String elf = (String) newChild.get("elf");
                Child childToAdd = new Child(id, lastName, firstName, age, city, niceScore,
                        newGiftString, niceScoreBonus, elf);
                childrenToAdd.add(childToAdd);
            }
            JSONArray childrenUpdates = (JSONArray) change.get("childrenUpdates");
            ArrayList<ChildUpdate> newChildrenUpdates = new ArrayList<>();
            for (Object obj : childrenUpdates) {
                JSONObject newChildrenUpdate = (JSONObject) obj;
                Long newID = (Long) newChildrenUpdate.get("id");
                int id = newID.intValue();
                getvalue = (Long) newChildrenUpdate.get("niceScore");
                double niceScore = -1;
                if (getvalue != null) {
                    niceScore = getvalue.doubleValue();
                }
                ArrayList<String> newGiftString = new ArrayList<>();
                JSONArray giftsPreference = (JSONArray) newChildrenUpdate.get("giftsPreferences");
                for (Object string : giftsPreference) {
                    String giftStringToAdd = (String) string;
                    newGiftString.add(giftStringToAdd);
                }
                String elf = (String) newChildrenUpdate.get("elf");
                ChildUpdate updateToAdd = new ChildUpdate(id, niceScore, newGiftString, elf);
                newChildrenUpdates.add(updateToAdd);
            }
            String strategy = (String) change.get("strategy");
            AnnualChange annualChange = new AnnualChange(newSantaBudget, giftsToAdd, childrenToAdd,
                    newChildrenUpdates, strategy);
            annualChanges1.add(annualChange);
        }
        this.annualChanges = annualChanges1;

    }
}

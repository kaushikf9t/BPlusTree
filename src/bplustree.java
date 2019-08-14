import java.io.*;
import java.util.ArrayList;

/**
 * Tester code for B-Plus tree
 */

public class bplustree {

    public static void main(String[] args) {
        // write your code here
        File file = new File(args[0]);
        PrintWriter writerObj = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st = "";
            //BPlusTreeInstance tree = new BPlusTreeInstance();
            writerObj = new PrintWriter("output_file.txt");
            st = br.readLine();

            //Order is number of children
            //Degree is number of pairs on each node
            int treeOrder = Integer.parseInt(st.substring(st.indexOf(CommonConstants.OPEN_BRACKET)+1, st.indexOf(CommonConstants.CLOSE_BRACKET)));
            BPlusTreeInstance bplusTreeInstance = new BPlusTreeBuilder().setOrder(treeOrder).setDegree(treeOrder - 1).setRoot(null).createBPlusTree();

            while ((st = br.readLine()) != null){
                if(st.contains("Insert")){
                    int key = Integer.parseInt(st.substring(st.indexOf(CommonConstants.OPEN_BRACKET)+1, st.indexOf(CommonConstants.SEPARATOR)));
                    Double value = Double.parseDouble(st.substring(st.indexOf(CommonConstants.SEPARATOR)+1 , st.indexOf(CommonConstants.CLOSE_BRACKET)));
                    bplusTreeInstance.insert(key, value);

                } else if(st.contains("Search")){
                    //Case of a single value search
                    if(!st.contains(CommonConstants.SEPARATOR)){
                        DataNode bNode;
                        int keyToSearch = Integer.parseInt(st.substring((st.indexOf(CommonConstants.OPEN_BRACKET)+1), st.indexOf(CommonConstants.CLOSE_BRACKET)));
                        if(bplusTreeInstance.getRootNode() != null){
                            bNode = (DataNode)BPlusUtil.findDataNode(bplusTreeInstance.getRootNode(),keyToSearch);
                            if(bNode != null) {
                                if(bNode.keys.contains(keyToSearch)){
                                    int indexOfKey = bNode.keys.indexOf(keyToSearch);
                                    Double value = bNode.values.get(indexOfKey);
                                    writerObj.println(value);
                                } else{
                                    writerObj.println("Null");
                                }
                            } else {
                                writerObj.println("Null");

                            }

                        }
                        else{
                            writerObj.println("Null");
                        }

                    } else {
                        //Range Search
                        try {
                            String arg1 = st.substring(st.indexOf(CommonConstants.OPEN_BRACKET) + 1, st.indexOf(CommonConstants.SEPARATOR));
                            int startKey = Integer.parseInt(arg1);
                            String arg2 = st.substring(st.indexOf(CommonConstants.SEPARATOR) + 1, st.indexOf(CommonConstants.CLOSE_BRACKET)).trim();
                            int endKey = Integer.parseInt(arg2);
                            ArrayList<Double> valuesInRange = bplusTreeInstance.searchBetweenKeys(startKey, endKey);
                            StringBuilder builder = BPlusUtil.returnSearchStrings(valuesInRange);
                            writerObj.println(builder);

                        } catch(NumberFormatException ex){
                            throw new NumberFormatException("Check the arguments provided in the range search");
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }

                    }

                } else if(st.contains("Delete")){
                    int keyToDelete = Integer.parseInt(st.substring((st.indexOf(CommonConstants.OPEN_BRACKET)+1), st.indexOf(CommonConstants.CLOSE_BRACKET)));
                    bplusTreeInstance.delete(keyToDelete);

                }

            }
            br.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NumberFormatException e){
            e.printStackTrace();
        }
        finally {
            writerObj.close();
        }
    }

}


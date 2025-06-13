package ssd.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BufferUtil {
    public HashMap<Integer,String> makeMemory(List<String> orderList){
        HashMap<Integer,String> result = new HashMap<>();
        for(String commend : orderList){
            String[] commends = commend.split("_");
            String we = commends[1];
            int lba = Integer.parseInt(commends[2]);
            if(we.equals("W")){
                String value = commends[3];
                result.put(lba,value);
            }else if(we.equals("E")){
                int size = Integer.parseInt(commends[3]);
                for(int i=0;i<size;i++){
                    result.put(lba+i,"0x00000000");
                }
            }
        }
        return result;
    }

    public List<String> makeCommand(HashMap<Integer,String> mem){
        int commandOrder = 1;
        List<String> result = new ArrayList<>();
        int eraseStart = -1;
        for(int i=0;i<=100;i++){
            if(mem.get(i)!=null){
                // erase 스타트 지점
                if(mem.get(i).equals("0x00000000")){
                    if(eraseStart == -1){
                        eraseStart =i;
                    }
                }
            }else{
                // erase 종료 지점
                if(eraseStart != -1){
                    result.add(commandOrder+"_E_"+eraseStart+"_"+(i-eraseStart));
                    commandOrder++;
                    eraseStart = -1;
                }
            }
        }


        for(Integer lba : mem.keySet()){
            if(!mem.get(lba).equals("0x00000000")){
                result.add(commandOrder+"_W_"+mem.get(lba));
                commandOrder++;
            }
        }
        return result;
    }
}

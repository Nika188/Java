package pgdp.adventuin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.AbstractMap;
import pgdp.color.RgbColor;
import java.util.HashMap;
import pgdp.color.RgbColor8Bit;

public final class AdventuinParty {
    public static Map<HatType, List<Adventuin>> groupByHatType(List<Adventuin> adventuin){
        if(adventuin.isEmpty()){
            return new HashMap<>();
        }
        return adventuin.stream().collect(Collectors.groupingBy(Adventuin::getHatType));
    }
    public static void printLocalizedChristmasGreetings(List<Adventuin> adventuin) {
        if(adventuin.isEmpty()){
            return;
        }
        adventuin.stream().sorted((a,b) -> a.getHeight()-b.getHeight()).forEach(adv->{
            System.out.println(adv.getLanguage().getLocalizedChristmasGreeting(adv.getName()));
        });
    }
    public static Map<HatType, List<Adventuin>> getAdventuinsWithLongestNamesByHatType(List<Adventuin> adventuin) {
        return groupByHatType(adventuin).entrySet().stream().map(group -> {
            int Max = group.getValue().stream().map(Adventuin::getName).mapToInt(String::length).max().getAsInt();
            List<Adventuin> value = group.getValue().stream().filter(a -> a.getName().length() == Max).collect(Collectors.toList());
            return new AbstractMap.SimpleEntry<HatType, List<Adventuin>>(group.getKey(), value);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public static Map<Integer,Double> getAverageColorBrightnessByHeight(List<Adventuin> adventuin) {
        if(adventuin.isEmpty()){
            return new HashMap<>();
        }
        class Helper {
            int id;
            double brightness;

            public Helper(int _id, double _b) {
                id = _id;
                brightness = _b;
            }
        }

        return adventuin.stream().map(i -> {
            int id = Math.round((float) i.getHeight() / 10) * 10;
            RgbColor color = i.getColor().toRgbColor8Bit();
            double brightness = (0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue()) / 255;
            return new Helper(id, brightness);
        }).collect(Collectors.groupingBy(i -> i.id)).entrySet().stream().map(entry -> {
            double avgBrightness = entry.getValue().stream().mapToDouble(helper -> helper.brightness).average().getAsDouble();
            return new AbstractMap.SimpleEntry<Integer, Double>(entry.getKey(), avgBrightness);

        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public static Map<HatType, Double> getDiffOfAvgHeightDiffsToPredecessorByHatType(List<Adventuin> adventuin) {
        if(adventuin.isEmpty()){
            return new HashMap<>();
        }

        return groupByHatType(adventuin).entrySet().stream().map(entry -> {
            HatType key = entry.getKey();
            List<Adventuin> list = entry.getValue();

            Map<Integer, Double> diffs = list.stream().map(i -> {
                int index = list.indexOf(i);
                Adventuin other;
                if (index + 1 >= list.size()){
                    other = list.get(0);
                }else{
                    other = list.get(index + 1);
                }
                return i.getHeight() - other.getHeight();
            }).collect(Collectors.groupingBy((Integer i) -> {
                int value = i.intValue();
                if (value < 0){
                    return -1;
                }else if (value > 0){
                    return 1;
                }else{
                    return 0;
                }

            })).entrySet().stream().map(diff -> {
                double avgDiffInGroup ;
                if (diff.getValue().isEmpty()){
                    avgDiffInGroup=0.0;
                }else{
                    avgDiffInGroup = diff.getValue().stream().mapToDouble(i -> i).average().getAsDouble();
                }
                return new AbstractMap.SimpleEntry<Integer, Double>(diff.getKey(), avgDiffInGroup);

            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if (diffs.get(-1)-diffs.get(1)>0){
                return new AbstractMap.SimpleEntry<HatType, Double>(key, Math.abs(diffs.get(-1) - diffs.get(1)));
            }else{
                return new AbstractMap.SimpleEntry<HatType, Double>(key, Math.abs(diffs.get(1) - diffs.get(-1)));
            }


        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


}

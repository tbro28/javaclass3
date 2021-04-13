package edu.uw.exemplar.di;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Collector {
    private List<Greeting> list;
    private Map<String, Integer> map;
    private Set<Integer> set;
    private Properties props;
    /**
     * @param list the list to set
     */
    public final void setList(List<Greeting> list) {
        this.list = list;
    }
    /**
     * @param map the map to set
     */
    public final void setMap(Map<String, Integer> map) {
        this.map = map;
    }
    /**
     * @param set the set to set
     */
    public final void setSet(Set<Integer> set) {
        this.set = set;
    }
    /**
     * @param props the props to set
     */
    public final void setProps(Properties props) {
        this.props = props;
    }
    
    @Override
    public String toString() {
        return list.toString() + "\n"
             + set.toString() + "\n"
             + map.toString() + "\n"
             + props.toString();
    }
    
}

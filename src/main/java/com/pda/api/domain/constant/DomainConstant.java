package com.pda.api.domain.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * @Classname DomainConstant
 * @Description TODO
 * @Date 2022-09-04 20:28
 * @Created by AlanZhang
 */
public class DomainConstant {

    public static final Set<String> skinSet = new HashSet<String>(){
        {
            add("肌注 皮(  )");
            add("静滴 皮(  )");
            add("静推皮()");
            add("口服 皮(  )");
            add("皮内注射");
            add("皮内注射(儿童)");
            add("皮下注射");
            add(";皮下注射(儿童)");
        }
    };
}

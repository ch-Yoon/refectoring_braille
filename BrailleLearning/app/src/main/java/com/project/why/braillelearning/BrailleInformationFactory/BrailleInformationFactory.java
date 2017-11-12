package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.Menu;

/**
 * Created by hyuck on 2017-09-13.
 */

/**
 *  Factory pattern을 위한 interface
 */
public interface BrailleInformationFactory {
    GettingInformation getInformationObject(Menu menuName);
}

package cn.cuiot.dmp.common.validator;

import javax.validation.groups.Default;

/**
 * @author xiaotao
 * @description:
 * @date 2024/9/2 15:27
 */
public interface ValidGroup extends Default {

    interface Crud extends ValidGroup{
        interface Insert extends Crud{

        }

        interface Update extends Crud{

        }

        interface Query extends Crud{

        }

        interface Delete extends Crud{

        }
    }
}

package com.jinunn.common.constant;

/**
 * @author : JinDun
 * @date : 2021/6/13 23:13
 */
public class ProductConstant {

    public enum AttrEnum{
        //ATTR_TYPE_BASE 基本属性 ATTR_TYPE_SALE 销售属性
        ATTR_TYPE_BASE(1,"基本属性"),ATTR_TYPE_SALE(2,"销售属性");
        private final Integer code;
        private final String msg;

        AttrEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum ProductStatusEnum{
        //NEW_SPU 新建 SPU_UP 商品上架,SPU_DOWN 商品下架
        NEW_SPU(0,"新建"),
        SPU_UP(1,"商品上架"),
        SPU_DOWN(2,"商品下架");
        private final Integer code;
        private final String msg;

        ProductStatusEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}

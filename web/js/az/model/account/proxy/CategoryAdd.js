Ext.define('alexzam.his.model.account.proxy.CategoryAdd', {
    extend:'alexzam.his.model.account.proxy.Category',

    constructor:function(config){
        var ret = this.callParent(arguments);
        this.extraParams = {type:'e'};

        return ret;
    }
});
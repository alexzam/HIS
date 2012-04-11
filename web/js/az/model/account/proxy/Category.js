Ext.define('alexzam.his.model.account.proxy.Category', {
    extend:'Ext.data.proxy.Ajax',

    requires:['alexzam.his.model.account.Category'],

    model:'alexzam.his.model.account.Category',
    reader:{
        type:'json',
        root:'items'
    },

    pageParam: null,
    startParam: null,
    limitParam: null,

    constructor:function(config){
        var me = this;
        config.url = config.rootUrl + 'account/catdata';

        var ret = me.callParent(arguments);
        return ret;
    }
});
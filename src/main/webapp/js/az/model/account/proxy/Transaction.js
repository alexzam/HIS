Ext.define('alexzam.his.model.account.proxy.Transaction', {
    extend:'Ext.data.proxy.Ajax',

    requires:['alexzam.his.model.account.Transaction'],

    model:'alexzam.his.model.account.Transaction',
    reader:{
        type:'json',
        root:'items'
    },
    writer:'json',

    constructor:function(config){
        config.url = config.rootUrl + 'account/data';

        return this.callParent(arguments);
    }
});
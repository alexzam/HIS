Ext.define('alexzam.his.model.account.proxy.Category', {
    extend:'Ext.data.proxy.Ajax',

    requires:['alexzam.his.model.account.Category'],

    model:'alexzam.his.model.account.Category',
    reader:{
        type:'json',
        root:'items'
    },

    constructor:function(config){
        config.url = config.rootUrl + 'trcategory-data';

        return this.callParent(arguments);
    }
});
Ext.define('alexzam.his.model.account.proxy.Category', {
    extend:'Ext.data.proxy.Ajax',

    requires:['alexzam.his.model.account.Category'],

    model:'alexzam.his.model.account.Category',
    reader:{
        type:'json',
        root:'items'
    },

    url:Ext.conf.rootUrl + 'account/catdata',

    pageParam: null,
    startParam: null,
    limitParam: null
});
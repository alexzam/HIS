Ext.define('alexzam.his.model.manage.Accounts', {
    extend: 'Ext.data.Store',
    storeId: 'manageAccounts',

    requires:[
        'alexzam.his.model.manage.model.Accounts',

        'Ext.data.proxy.Ajax'
    ],

    model: 'alexzam.his.model.manage.model.Accounts',

    proxy: {
        type: 'ajax',
        reader:{
            type:'json',
            root:'items'
        },

        url:Ext.conf.rootUrl + 'manage/accounts',

        pageParam: null,
        startParam: null,
        limitParam: null
    },

    autoLoad:true,
    autoSync:true,

    sorters: [{
        property: 'name',
        direction: 'ASC'
    }]
});


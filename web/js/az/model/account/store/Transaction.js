Ext.define('alexzam.his.model.account.store.Transaction', {
    extend: 'Ext.data.Store',
    requires:[
        'alexzam.his.model.account.Transaction'
    ],

    model:'alexzam.his.model.account.Transaction',
    autoLoad:true
});
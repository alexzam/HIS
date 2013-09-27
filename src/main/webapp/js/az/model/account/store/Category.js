Ext.define('alexzam.his.model.account.store.Category', {
    extend: 'Ext.data.Store',
    requires:[
        'alexzam.his.model.account.Category'
    ],

    model:'alexzam.his.model.account.Category',
    autoLoad:true,

    sorters: [{
        property: 'name',
        direction: 'ASC'
    }]
});
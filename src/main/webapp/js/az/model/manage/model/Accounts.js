Ext.define('alexzam.his.model.manage.model.Accounts', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'val', type: 'int'},
        {name: 'privacy', type: 'string'}
    ]
});
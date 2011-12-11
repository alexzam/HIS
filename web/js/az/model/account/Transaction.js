Ext.define('alexzam.his.model.account.Transaction', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'string'},
        {name: 'actor_id', type: 'int'},
        {name: 'actor_name', type: 'string'},
        {name: 'amount', type: 'float'},
        {name: 'category_name', type: 'string'},
        {name: 'comment', type: 'string'},
        {name: 'type', type: 'string'},
        {name: 'timestamp', type: 'date', dateFormat: 'd.m.Y'}
    ]
});
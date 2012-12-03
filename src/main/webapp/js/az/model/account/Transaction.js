Ext.define('alexzam.his.model.account.Transaction', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'string'},
        {name: 'actor_id', type: 'int'},
        {name: 'actor_name', type: 'string', persist:false},
        {name: 'amount', type: 'float'},
        {name: 'category_name', type: 'string', persist:false},
        {name: 'category_id', type: 'string'},
        {name: 'comment', type: 'string'},
        {name: 'type', type: 'string', persist:false},
        {name: 'timestamp', type: 'date', dateFormat: 'd.m.Y',
            serialize: function(val){
                return val.getTime();
            }
        }
    ]
});
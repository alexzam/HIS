Ext.define('alexzam.his.model.reports.DateVal', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'date', type: 'date', dateFormat: 'd.m.Y'},
        {name: 'value', type: 'float'},
        {name:'cat', type:'string'}
    ]
});
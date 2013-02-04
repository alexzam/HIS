Ext.define('alexzam.his.view.reports.CatSpendColumnChart', {
    extend:'Ext.chart.Chart',

    requires:[
        'alexzam.his.model.reports.CatVal',
        'Ext.chart.theme.Base',
        'Ext.chart.series.Bar',
        'Ext.chart.axis.Numeric',
        'Ext.chart.axis.Category',
        'Ext.data.Request',
        'Ext.data.Store',
        'Ext.data.reader.Json'
    ],

    alias:'widget.his.chart.catspend',

    store:null,
    animate: true,

    axes: [
        {
            type: 'Numeric',
            position: 'bottom',
            fields: ['value'],
            title: 'Потрачено',
            minimum: 0
        },
        {
            type: 'Category',
            position: 'left',
            fields: ['name'],
            title: 'Категория'
        }
    ],

    series: [
        {
            type: 'bar',
            axis: 'bottom',
            xField: 'name',
            yField: 'value',
            highlight: true,
            label: {
                display: 'insideEnd',
                field: 'value',
                renderer: Ext.util.Format.numberRenderer('0'),
                orientation: 'horizontal',
                color: '#333',
                'text-anchor': 'middle'
            }
        }
    ],

    initComponent:function () {
        var me = this;

        me.store = Ext.create('Ext.data.Store',
        {
            model:'alexzam.his.model.reports.CatVal',
            proxy:{
                type:'ajax',
                url:Ext.rootUrl + 'reports/data/spendbycat',
                reader:{
                    type:'json',
                    root:'items'
                }
            }
        });

        me.callParent();

        me.store.load();
    }
});
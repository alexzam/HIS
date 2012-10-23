Ext.define('alexzam.his.view.reports.ExpensesLineChart', {
    extend:'Ext.chart.Chart',

    requires:[
        'alexzam.his.model.reports.DateVal',
        'Ext.chart.theme.Base',
        'Ext.chart.axis.Numeric',
        'Ext.chart.axis.Time',
        'Ext.chart.series.Line',
        'Ext.data.Request',
        'Ext.data.Store',
        'Ext.data.reader.Json'
    ],

    alias:'widget.his.chart.expense',

    store:null,
    animate: true,

    axes: [
        {
            type: 'Time',
            position: 'bottom',
            fields: ['date'],
            title: 'Дата',
            step: [Ext.Date.DAY, 1]
        },
        {
            type: 'Numeric',
            position: 'left',
            fields: ['value'],
            title: 'Сумма'
        }
    ],

    series: [
        {
            type: 'line',
            axis: 'left',
            xField: 'date',
            yField: 'value',
            highlight: true,
            markerConfig: {
                type: 'circle',
                size: 4,
                radius: 4,
                'stroke-width': 1
            }
        }
    ],

    initComponent:function () {
        var me = this;

        me.store = Ext.create('Ext.data.Store',
        {
            model:'alexzam.his.model.reports.DateVal',
            proxy:{
                type:'ajax',
                url:Ext.rootUrl + 'reports/data/expenses',
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
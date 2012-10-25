Ext.define('alexzam.his.view.reports.ExpensesLineChart', {
    extend:'Ext.panel.Panel',

    alias:'widget.his.chart.expense',

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

    layout:'border',

    items:[
        {
            xtype:'panel',
            itemId:'pnlChart',

            region:'center',

            layout:'fit',
            items:[
                {
                    xtype:'chart',
                    itemId:'chart',

                    store:null,
                    animate: true,
                    autoRender:true,

                    axes: [
                        {
                            type: 'Time',
                            position: 'bottom',
                            fields: ['date'],
                            title: 'Дата',
                            dateFormat: 'M j',
                            step: [Ext.Date.DAY, 1]
                        },
                        {
                            type: 'Numeric',
                            position: 'left',
                            fields: ['value'],
                            title: 'Сумма'
                        }
                    ],

                    series: []
                }
            ]
        },
        {
            xtype:'form',
            itemId:'filterForm',

            region:'east',

            bodyPadding:5,
            layout:{
                type:'vbox',
                align:'stretch',
                constrainAlign:true
            },
            bubbleEvents:['filterchange'],
            items:[
                {
                    xtype:'datefield',
                    fieldLabel:'С',
                    name:'from',
                    format:'d.m.Y',
                    validateOnChange:false,
                    labelWidth:65,
                    itemId:'dtFrom',
                    startDay:1,
                    listeners:{
                        change:function() {
                            this.ownerCt.fireEvent('filterchange')
                        }
                    }
                },
                {
                    xtype:'datefield',
                    fieldLabel:'По',
                    name:'to',
                    format:'d.m.Y',
                    validateOnChange:false,
                    labelWidth:65,
                    itemId:'dtTo',
                    startDay:1,
                    listeners:{
                        change:function() {
                            this.ownerCt.fireEvent('filterchange')
                        }
                    }
                }
            ]
        }
    ],

    initComponent:function () {
        var me = this;
        var chart = me.items[0].items[0];

        chart.store = Ext.create('Ext.data.Store',
        {
            model:'alexzam.his.model.reports.DateVal',
            proxy:{
                type:'ajax',
                url:Ext.rootUrl + 'reports/data/expenses',
                reader:{
                    type:'json',
                    root:'items'
                }
            },
            sorters:[
                {
                    property:'date'
                }
            ],
            listeners:{
                load:function(store) {
                    var data = store.getProxy().getReader().rawData;
                    Ext.each(data.series, function(serie) {
                        chart.series.add({
                            type: 'line',
                            axis: 'left',
                            xField: 'date',
                            yField: serie.field,
                            highlight: true,
                            markerConfig: {
                                type: 'circle',
                                size: 4,
                                radius: 4,
                                'stroke-width': 1
                            }
                        });
                    });
                    chart.refresh();
                }
            }
        });

        me.callParent();

        me.chart = me.getComponent('pnlChart').getComponent('chart');
        me.chart.store.load();
    },

    listeners:{
        filterchange:function(){
            var me = this;
            me.chart.store.load();
        }
    }
});
Ext.define('alexzam.his.ReportsScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.toolbar.Toolbar',
        'alexzam.his.view.reports.CatSpendColumnChart',
        'alexzam.his.view.reports.ExpensesLineChart',
        'Ext.layout.container.Border'
    ],

    layout:'fit',
    items:[
        {
            // Outer panel
            xtype:'panel',
            layout:'fit',
            dockedItems:[
                {
                    xtype:'his.Toolbar',
                    dock:'top',
                    disableId:'rep'
                }
            ],
            items:[
                {
                    xtype:'panel',
                    layout:'border',
                    title:"Расходы по категориям",
                    items:[
                        {
                            // Filter panel
                            xtype:'panel',
                            region:'north',
                            html:'filters here'
                        },
                        {
                            xtype:'panel',
                            region:'center',
                            layout:'fit',
                            items:[
                                {
                                    xtype:'his.chart.expense'
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ]
});
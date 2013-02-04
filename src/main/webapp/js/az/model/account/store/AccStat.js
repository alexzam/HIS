Ext.define('alexzam.his.model.account.store.AccStat', {
    extend:'Ext.data.Store',
    requires:[
        'alexzam.his.model.account.AccStat'
    ],

    model:'alexzam.his.model.account.AccStat',
    data:[
        {id:'TE', name:'Общие расходы', val:'0'},
        {id:'EE', name:'На каждого', val:'0'},
        {id:'PE', name:'Мои траты', val:'0'},
        {id:'PD', name:'Мой вклад в Казну', val:'0'},
        {id:'PS', name:'Всего моих расходов', val:'0'},
        {id:'PB', name:'Мой баланс', val:'0'}
    ]
});
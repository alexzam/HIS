/**
 * Config common ExtJS params.
 */
Ext.BLANK_IMAGE_URL = 'js/ext/resources/images/default/s.gif';

Ext.util.Format.thousandSeparator = ' ';
Ext.util.Format.decimalSeparator = ',';

Ext.Date.monthNumbers = {
    'Янв':0,
    'Фев':1,
    'Мар':2,
    'Апр':3,
    'Май':4,
    'Июн':5,
    'Июл':6,
    'Авг':7,
    'Сен':8,
    'Окт':9,
    'Ноя':10,
    'Дек':11
};

Ext.Loader.setConfig({
    enabled: true,
    paths: {
        'alexzam.his': rootUrl + 'js/az',
        'Ext': rootUrl + 'js/ext/ext'
    }
});

var az ={
   fixDraw:function(){
        Ext.draw.Draw.snapEndsByDateAndStep = function(from, to, step, lockEnds) {
          var fromStat = [from.getFullYear(), from.getMonth(), from.getDate(),
                  from.getHours(), from.getMinutes(), from.getSeconds(), from.getMilliseconds()],
              steps = 0, testFrom, testTo;
          if (lockEnds) {
              testFrom = from;
          } else {
              switch (step[0]) {
                  case Ext.Date.MILLI:
                      testFrom = new Date(fromStat[0], fromStat[1], fromStat[2], fromStat[3],
                              fromStat[4], fromStat[5], Math.floor(fromStat[6] / step[1]) * step[1]);
                      break;
                  case Ext.Date.SECOND:
                      testFrom = new Date(fromStat[0], fromStat[1], fromStat[2], fromStat[3],
                              fromStat[4], Math.floor(fromStat[5] / step[1]) * step[1], 0);
                      break;
                  case Ext.Date.MINUTE:
                      testFrom = new Date(fromStat[0], fromStat[1], fromStat[2], fromStat[3],
                              Math.floor(fromStat[4] / step[1]) * step[1], 0, 0);
                      break;
                  case Ext.Date.HOUR:
                      testFrom = new Date(fromStat[0], fromStat[1], fromStat[2],
                              Math.floor(fromStat[3] / step[1]) * step[1], 0, 0, 0);
                      break;
                  case Ext.Date.DAY:
////// FIX IS HERE:
                      testFrom = new Date(fromStat[0], fromStat[1],
                              Math.floor((fromStat[2] - 1) / step[1]) * step[1] + 1, 0, 0, 0, 0);
                      break;
                  case Ext.Date.MONTH:
                      testFrom = new Date(fromStat[0], Math.floor(fromStat[1] / step[1]) * step[1], 1, 0, 0, 0, 0);
                      break;
                  default: // Ext.Date.YEAR
                      testFrom = new Date(Math.floor(fromStat[0] / step[1]) * step[1], 0, 1, 0, 0, 0, 0);
                      break;
              }
          }

          testTo = testFrom;
          // TODO(zhangbei) : We can do it better somehow...
          while (testTo < to) {
              testTo = Ext.Date.add(testTo, step[0], step[1]);
              steps++;
          }

          if (lockEnds) {
              testTo = to;
          }
          return {
              from : +testFrom,
              to : +testTo,
              step : (testTo - testFrom) / steps,
              steps : steps
          };
      }
   }
};
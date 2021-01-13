<table>
    <col width="25%">
    <col width="35%">
    <col width="40%">
    <thead>
    <tr>
        <th>Scenario</th>
        <th>Payload</th>
        <th>Response</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td><p>Benefits and Credits data found </p>
        <td>
            <p>matchId=&lt;obtained from Individuals Matching API example: 57072660-1df9-4aeb-b4ea-cd2d7f96e430&gt;</p>
            <p>startTaxYear=2018-19<br>endTaxYear= 2019-20</p>
        </td>
        <td><p>200 (OK)</p><p>Payload as response example above</p></td>
    </tr>
    <tr>
        <td><p>Missing MatchId</p></td>
        <td>
            <p>matchId query parameter missing</p>
        </td>
        <td><p>400 (Bad Request)</p>
        <p>{ &quot;code&quot; : &quot;INVALID_REQUEST&quot;,<br/>&quot;message&quot; : &quot;matchId is required&quot; }</p></td>
    </tr>
    <tr>
          <td><p>Incorrect matchId</p></td>
          <td>The matchId is not valid</td>
          <td><p>404 (Not Found)</p>
          <p>{ &quot;code&quot; : &quot;NOT_FOUND&quot;,<br/>&quot;message&quot; : &quot;The resource cannot be found&quot; }</p></td>
    </tr>
  </tbody>
</table>